package com.dbn.cache;

import com.dbn.browser.DatabaseBrowserManager;
import com.dbn.connection.ConnectionId;
import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.object.type.DBObjectType;
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
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.Collection;
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
    private static final String IDENTIFIER_VIEWS = "VIEWS";
    private static final String IDENTIFIER_ALL_COLUMNS = "ALL_COLUMNS";
    private static final String IDENTIFIER_ALL_INDEXES = "ALL_INDEXES";
    private static final String IDENTIFIER_ALL_INDEX_COLUMNS = "ALL_INDEX_COLUMNS";

    private final Map<String, Map<String, CacheDbTable>> schemaMap = Maps.newHashMap();

    public static MetadataCacheService getService(Project project) {
        return project.getService(MetadataCacheService.class);
    }

    @SuppressWarnings("unused")
    public @Nullable Map<String, CacheDbTable> getFirstConnectionDBCacheTables(Project project) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        String dbName = browserManager.getFirstConnectionConfigDbName(project);
        return schemaMap.get(dbName);
    }

    public void initFirstConnectionCacheDbTable(@NotNull Project project) {
        DatabaseBrowserManager browserManager = DatabaseBrowserManager.getInstance(project);
        ConnectionId connectionId = browserManager.getFirstConnectionId(project);
        if (connectionId == null) {
            return;
        }

        String dbName = browserManager.getFirstConnectionConfigDbName(project);
        if (dbName == null) {
            return;
        }

        MetadataCacheService cacheService = MetadataCacheService.getService(project);
        // 从本地缓存中初始化数据库元数据信息
        cacheService.initCacheDbTable(dbName, project, connectionId.id());
    }

    public void initCacheDbTable(String schemaName, Project project, String connectionId) {
        String fileFullName = getCacheFileFullName(schemaName, project, connectionId);

        ObjectNode rootObjectNode = JsonUtils.readTree(fileFullName);
        if (rootObjectNode == null) {
            return;
        }

        Map<String, CacheDbTable> tableMap = Maps.newLinkedHashMap();

        ArrayNode identifierTableArrayNode = (ArrayNode) rootObjectNode.get(IDENTIFIER_TABLES);
        if (identifierTableArrayNode != null) {
            for (JsonNode jsonNode : identifierTableArrayNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;

                CacheDbTable cacheDbTable = createCacheDbTable(objectNode);

                tableMap.put(cacheDbTable.getName(), cacheDbTable);
            }
            log.warn("初始化{}个表", identifierTableArrayNode.size());
        }

        ArrayNode identifierViewArrayNode = (ArrayNode) rootObjectNode.get(IDENTIFIER_VIEWS);
        if (identifierViewArrayNode != null) {
            for (JsonNode jsonNode : identifierViewArrayNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;

                CacheDbTable cacheDbTable = createCacheDbTableView(objectNode);

                tableMap.put(cacheDbTable.getName(), cacheDbTable);
            }
            log.warn("初始化{}个视图", identifierViewArrayNode.size());
        }

        int columnSize = 0;
        ArrayNode identifierColumnArrayNode = (ArrayNode) rootObjectNode.get(IDENTIFIER_ALL_COLUMNS);
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

        int indexSize = 0;
        ArrayNode identifierIndexArrayNode = (ArrayNode) rootObjectNode.get(IDENTIFIER_ALL_INDEXES);
        ArrayNode identifierIndexColumnArrayNode = (ArrayNode) rootObjectNode.get(IDENTIFIER_ALL_INDEX_COLUMNS);
        if (identifierIndexArrayNode != null && identifierIndexColumnArrayNode != null) {

            MultiValueMap multiValueMap = convertIndexColumnToMap(identifierIndexColumnArrayNode);

            for (JsonNode jsonNode : identifierIndexArrayNode) {
                ObjectNode objectNode = (ObjectNode) jsonNode;
                String tableName = objectNode.get("TABLE_NAME").asText("");
                CacheDbTable cacheDbTable = tableMap.get(tableName);
                if (cacheDbTable == null) {
                    continue;
                }

                String indexName = objectNode.get("INDEX_NAME").asText();
                @SuppressWarnings("unchecked")
                Collection<String> columnNames = (Collection<String>) multiValueMap.get(tableName + "-" + indexName);
                if (columnNames == null) {
                    continue;
                }

                CacheDbIndex cacheDbIndex = createCacheDbIndex(objectNode, indexName, columnNames);

                cacheDbTable.addCacheDbIndex(cacheDbIndex);

                indexSize++;
            }
        }

        schemaMap.put(schemaName, tableMap);
        log.warn("初始化schemaMap成功,schemaName:{},表数量:{},列数量:{},索引数量:{}", schemaName, tableMap.size(), columnSize, indexSize);
    }

    private MultiValueMap convertIndexColumnToMap(ArrayNode identifierIndexColumnArrayNode) {
        MultiValueMap map = new MultiValueMap();
        for (JsonNode indexColumNode : identifierIndexColumnArrayNode) {
            String indexName = indexColumNode.get("INDEX_NAME").asText();
            String tableName = indexColumNode.get("TABLE_NAME").asText();
            String columnName = indexColumNode.get("COLUMN_NAME").asText();
            map.put(tableName + "-" + indexName, columnName);
        }
        return map;
    }

    private CacheDbTable createCacheDbTable(ObjectNode objectNode) {
        String tableName = objectNode.get("TABLE_NAME").asText("");
        String tableComment = objectNode.get("TABLE_COMMENT").asText("");
        boolean isTemporary = isYesFlag(objectNode.get("IS_TEMPORARY").asText());
        return new CacheDbTable(tableName, tableComment, isTemporary);
    }

    private CacheDbTable createCacheDbTableView(ObjectNode objectNode) {
        String viewName = objectNode.get("VIEW_NAME").asText("");
        String viewComment = objectNode.get("VIEW_COMMENT").asText("");
        return new CacheDbTable(viewName, viewComment, false);
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

    private CacheDbIndex createCacheDbIndex(ObjectNode objectNode, String name, Collection<String> columnNames) {
        boolean isUnique = isYesFlag(objectNode.get("IS_UNIQUE").asText());
        boolean isValid = isYesFlag(objectNode.get("IS_VALID").asText());
        return new CacheDbIndex(name, isUnique, isValid, columnNames);
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
    public synchronized ArrayNode saveResultSetToLocal(String schemaName, Project project, ResultSet resultSet, String connectionId,
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
        resultSet.close();

        JsonUtils.saveTree(rootObjectNode, fileFullName);
        log.warn("完成缓存元数据:{},共{}个子元素,路径:{}", identifier, identifierArrayNode.size(), fileFullName);

        if (StringUtils.isNotBlank(schemaName)) {
            initCacheDbTable(schemaName, project, connectionId);
        }

        return identifierArrayNode;
    }

    public synchronized void clearCache(String schemaName, Project project, DBNConnection connection, DBObjectType objectType) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection.getId().id());
        File file = new File(fileFullName);
        if (!file.exists()) {
            return;
        }

        if (schemaName == null) {
            return;
        }

        ObjectNode rootObjectNode = JsonUtils.readTree(fileFullName);
        if (rootObjectNode == null) {
            return;
        }

        List<String> delNames = getDelStrings(objectType);

        for (String delName : delNames) {
            JsonNode jsonNode = rootObjectNode.remove(delName);
            log.warn("清除元数据:{},共{}个子元素,路径:{}", delName, jsonNode.size(), fileFullName);
        }

        JsonUtils.saveTree(rootObjectNode, fileFullName);
    }

    private List<String> getDelStrings(DBObjectType objectType) {
        List<String> delNames = Lists.newArrayList();
        if (objectType == DBObjectType.INDEX) {
            delNames.add("DATASET_INDEXES");
            delNames.add("ALL_INDEXES");
        } else if (objectType == DBObjectType.CONSTRAINT) {
            delNames.add("DATASET_CONSTRAINTS");
            delNames.add("ALL_CONSTRAINTS");
        } else if (objectType == DBObjectType.COLUMN) {
            delNames.add("DATASET_COLUMNS");
            delNames.add("ALL_COLUMNS");
        } else if (objectType == DBObjectType.TABLE) {
            delNames.add("TABLES");
        }
        return delNames;
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
