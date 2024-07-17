package com.dbn.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String STANDARD_PATTERN = "yyyy-MM-dd HH:mm:ss";

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.setTimeZone(TimeZone.getDefault());
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(STANDARD_PATTERN));
    }

    @SneakyThrows
    public static synchronized ObjectNode readTree(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        JsonNode jsonNode = OBJECT_MAPPER.readTree(new File(filePath));
        if (jsonNode instanceof ObjectNode) {
            return (ObjectNode) jsonNode;
        }

        return null;
    }

    @SneakyThrows
    public static synchronized void saveTree(ObjectNode objectNode, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentPath = file.getParentFile();
            if (!parentPath.exists()) {
                Files.createDirectories(parentPath.toPath());
            }
            Files.createFile(file.toPath());
        }

        OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, objectNode);
    }
}
