package com.study.contactapi.utils.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

@Service
public class DataHandler {
    @Autowired
    private ObjectMapper objectMapper;

    /* https://stackoverflow.com/questions/38424383/how-to-distinguish-between-null-and-not-provided-values-for-partial-updates-in-s */
    public <T> T mergeData(Object source, Object target) {
        ObjectReader readerForUpdating = objectMapper.readerForUpdating(source);

        try {
            return readerForUpdating.readValue(objectMapper.convertValue(target, JsonNode.class));
        } catch (Exception exception) {
            return null;
        }
    }
}
