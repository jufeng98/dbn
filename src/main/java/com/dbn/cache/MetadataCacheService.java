package com.dbn.cache;

import com.dbn.connection.jdbc.DBNConnection;
import com.dbn.utils.JsonUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.List;

import static com.dbn.utils.JsonUtils.OBJECT_MAPPER;

/**
 * 数据库元数据本地缓存服务,用于将元数据信息缓存到本地,避免频繁请求数据库,提高效率
 */
@Slf4j
@Service(Service.Level.PROJECT)
public final class MetadataCacheService {

    public static MetadataCacheService getService(Project project) {
        return project.getService(MetadataCacheService.class);
    }

    public CacheResultSet loadCacheResultSet(String schemaName, Project project, ResultSet resultSet, DBNConnection connection,
                                             String identifier) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection);
        ObjectNode objectNode = JsonUtils.readTree(fileFullName);
        if (objectNode == null) {
            return null;
        }

        ArrayNode identifierArrayNode = (ArrayNode) objectNode.get(identifier);
        if (identifierArrayNode == null) {
            return null;
        }

        log.warn("从本地缓存中载入:{},共{}个子元素", identifier, identifierArrayNode.size());
        return new CacheResultSet(resultSet, connection, identifierArrayNode, identifier);
    }

    private String getCacheFileFullName(String schemaName, Project project, DBNConnection connection) {
        String name = connection.getId().id();
        if (schemaName != null) {
            name = name + "-" + schemaName;
        }

        return project.getBasePath() + "/.idea/dataSource/" + name + ".json";
    }

    @SneakyThrows
    public void saveResultSetToLocal(String schemaName, Project project, ResultSet resultSet, DBNConnection connection,
                                     String identifier) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection);

        ObjectNode rootObjectNode = JsonUtils.readTree(fileFullName);
        if (rootObjectNode == null) {
            rootObjectNode = OBJECT_MAPPER.createObjectNode();
        }
        rootObjectNode.put("name", connection.getName());

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
    }

    public void clearCache(String schemaName, Project project, DBNConnection connection) {
        String fileFullName = getCacheFileFullName(schemaName, project, connection);
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
