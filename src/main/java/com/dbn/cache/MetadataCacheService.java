package com.dbn.cache;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dbn.utils.JsonUtils.OBJECT_MAPPER;

/**
 * 数据库元数据本地缓存服务,用于将元数据信息缓存到本地,避免频繁请求数据库,提高效率
 */
@Getter
@Slf4j
@Service(Service.Level.PROJECT)
public final class MetadataCacheService {
    private static final String IDENTIFIER_TABLES = "TABLES";
    private static final String IDENTIFIER_ALL_COLUMNS = "ALL_COLUMNS";

    private final Map<String, Map<String, CacheDbTable>> schemaMap = Maps.newHashMap();

    public static MetadataCacheService getService(Project project) {
        return project.getService(MetadataCacheService.class);
    }

    public void initCacheDbTable(String schemaName, Project project, String connectionId) {
        String fileFullName = getCacheFileFullName(schemaName, project, connectionId);

        ArrayNode identifierTableArrayNode = loadArrayNode(fileFullName, IDENTIFIER_TABLES);
        if (identifierTableArrayNode == null) {
            return;
        }

        Map<String, CacheDbTable> tableMap = Maps.newLinkedHashMap();
        for (JsonNode jsonNode : identifierTableArrayNode) {
            ObjectNode objectNode = (ObjectNode) jsonNode;

            CacheDbTable cacheDbTable = createCacheDbTable(objectNode);

            tableMap.put(cacheDbTable.getName(), cacheDbTable);
        }

        int columnSize = 0;
        ArrayNode identifierColumnArrayNode = loadArrayNode(fileFullName, IDENTIFIER_ALL_COLUMNS);
        if (identifierColumnArrayNode != null) {
            for (JsonNode jsonNode : identifierColumnArrayNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                String datasetName = objectNode.get("DATASET_NAME").asText("");
                CacheDbTable cacheDbTable = tableMap.get(datasetName);
                if (cacheDbTable == null) {
                    continue;
                }

                CacheDbColumn cacheDbColumn = createCacheDbColumn(objectNode);

                cacheDbTable.addCacheDbColumn(cacheDbColumn);

                columnSize++;
            }
        }

        schemaMap.put(schemaName, tableMap);
        log.warn("初始化schemaMap成功,schemaName:{},表数量:{},列数量:{}", schemaName, schemaMap.size(), columnSize);
    }

    private CacheDbTable createCacheDbTable(ObjectNode objectNode) {
        String tableName = objectNode.get("TABLE_NAME").asText("");
        String tableComment = objectNode.get("TABLE_COMMENT").asText("");
        boolean isTemporary = isYesFlag(objectNode.get("IS_TEMPORARY").asText());
        return new CacheDbTable(tableName, tableComment, isTemporary);
    }

    private CacheDbColumn createCacheDbColumn(ObjectNode objectNode) {
        String columnName = objectNode.get("COLUMN_NAME").asText("");
        String columnComment = objectNode.get("COLUMN_COMMENT").asText("");
        String columnDefault = objectNode.get("COLUMN_DEFAULT").asText("");
        short position = objectNode.get("POSITION").shortValue();
        boolean isNullable = isYesFlag(objectNode.get("IS_NULLABLE").asText());
        boolean isHidden = isYesFlag(objectNode.get("IS_HIDDEN").asText());
        boolean isPrimaryKey = isYesFlag(objectNode.get("IS_PRIMARY_KEY").asText());
        boolean isForeignKey = isYesFlag(objectNode.get("IS_FOREIGN_KEY").asText());
        boolean isUniqueKey = isYesFlag(objectNode.get("IS_UNIQUE_KEY").asText());
        boolean isIdentity = isYesFlag(objectNode.get("IS_IDENTITY").asText());

        CacheDbColumn cacheDbColumn = new CacheDbColumn(columnName, columnComment, columnDefault, position,
                isNullable, isHidden, isPrimaryKey, isForeignKey, isUniqueKey, isIdentity);

        String dataTypeName = objectNode.get("DATA_TYPE_NAME").asText("");
        long dataLength = objectNode.get("DATA_LENGTH").asLong();
        int dataPrecision = objectNode.get("DATA_PRECISION").asInt();
        int dataScale = objectNode.get("DATA_SCALE").asInt();
        boolean isSet = objectNode.get("IS_SET").asBoolean();

        cacheDbColumn.cacheDbDataType = new CacheDbDataType(dataTypeName, dataLength, dataPrecision, dataScale, isSet);

        return cacheDbColumn;
    }

    public CacheResultSet loadCacheResultSet(String schemaName, Project project, ResultSet resultSet,
                                             DBNConnection connection, String identifier) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection.getId().id());

        ArrayNode identifierArrayNode = loadArrayNode(fileFullName, identifier);
        if (identifierArrayNode == null) {
            return null;
        }

        log.warn("从本地缓存中载入:{},共{}个子元素构建cacheResultSet", identifier, identifierArrayNode.size());
        return new CacheResultSet(resultSet, connection, identifierArrayNode, identifier);
    }

    private ArrayNode loadArrayNode(String fileFullName, String identifier) {
        ObjectNode objectNode = JsonUtils.readTree(fileFullName);
        if (objectNode == null) {
            return null;
        }

        return (ArrayNode) objectNode.get(identifier);
    }

    private String getCacheFileFullName(String schemaName, Project project, String connectionId) {
        String name = connectionId;
        if (schemaName != null) {
            name = name + "-" + schemaName;
        }

        return project.getBasePath() + "/.idea/dataSource/" + name + ".json";
    }

    @SneakyThrows
    public void saveResultSetToLocal(String schemaName, Project project, ResultSet resultSet, String connectionId,
                                     String identifier) {
        String fileFullName = getCacheFileFullName(schemaName, project, connectionId);

        ObjectNode rootObjectNode = JsonUtils.readTree(fileFullName);
        if (rootObjectNode == null) {
            rootObjectNode = OBJECT_MAPPER.createObjectNode();
        }
        rootObjectNode.put("name", schemaName);

        ArrayNode identifierArrayNode = rootObjectNode.putArray(identifier);

        List<String> labels = Lists.newArrayList();
        val metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            val columnLabel = metaData.getColumnLabel(i);
            labels.add(columnLabel);
        }

        while (resultSet.next()) {
            ObjectNode objectNode = identifierArrayNode.addObject();
            for (String columnLabel : labels) {
                val columnValue = resultSet.getObject(columnLabel);
                put(objectNode, columnLabel, columnValue);
            }
        }
        resultSet.beforeFirst();

        JsonUtils.saveTree(rootObjectNode, fileFullName);
        log.warn("完成缓存元数据:{},共{}个子元素,路径:{}", identifier, metaData.getColumnCount(), fileFullName);

        if (StringUtils.isNotBlank(schemaName)) {
            initCacheDbTable(schemaName, project, connectionId);
        }
    }

    public void clearCache(String schemaName, Project project, DBNConnection connection) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection.getId().id());
        File file = new File(fileFullName);
        if (!file.exists()) {
            return;
        }

        if (schemaName != null) {
            boolean delete = file.delete();
            log.warn("删除缓存文件:{},结果:{}", file, delete);
            return;
        }

        File parentFile = file.getParentFile();
        File[] files = parentFile.listFiles();
        if (files == null) {
            return;
        }

        for (File pathFile : files) {
            if (!pathFile.getName().startsWith(connection.getId().id())) {
                continue;
            }
            boolean delete = pathFile.delete();
            log.warn("循环删除缓存文件:{},结果:{}", pathFile, delete);
        }
    }

    private boolean isYesFlag(String columnValue) {
        return Objects.equals(columnValue, "Y");
    }

    private void put(ObjectNode objectNode, String columnLabel, Object columnValue) {
        if (columnValue == null) {
            objectNode.putNull(columnLabel);
            return;
        }

        if (columnValue instanceof String) {
            objectNode.put(columnLabel, (String) columnValue);
        } else if (columnValue instanceof Short) {
            objectNode.put(columnLabel, (Short) columnValue);
        } else if (columnValue instanceof Integer) {
            objectNode.put(columnLabel, (Integer) columnValue);
        } else if (columnValue instanceof Long) {
            objectNode.put(columnLabel, (Long) columnValue);
        } else if (columnValue instanceof Double) {
            objectNode.put(columnLabel, (Double) columnValue);
        } else if (columnValue instanceof Float) {
            objectNode.put(columnLabel, (Float) columnValue);
        } else if (columnValue instanceof Boolean) {
            objectNode.put(columnLabel, (Boolean) columnValue);
        } else if (columnValue instanceof BigInteger) {
            objectNode.put(columnLabel, (BigInteger) columnValue);
        } else {
            log.warn("类型:{},value:{}", columnValue.getClass(), columnValue);
        }
    }
}
