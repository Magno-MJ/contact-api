package com.study.contactapi.utils.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataHandlerTest {
    @Mock
    ObjectMapper objectMapper;

    @Autowired
    @InjectMocks
    DataHandler dataHandler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should merge data successfully")
    void mergeDataCase1() throws IOException {
        String createdObject = "fake-object-string";

        ObjectReader objectReader = mock(ObjectReader.class);
        when(objectReader.readValue(objectMapper.convertValue(Object.class, JsonNode.class))).thenReturn(createdObject);

        when(objectMapper.readerForUpdating(any())).thenReturn(objectReader);

        String firstObject = "fake";
        String secondObject = "fake";

        Object result = dataHandler.mergeData(firstObject, secondObject);

        assertThat(result).isEqualTo(createdObject);

        verify(objectMapper, times(1)).readerForUpdating(firstObject);
        verify(objectMapper, times(1)).convertValue(secondObject,JsonNode.class);
        verify(objectReader, times(1)).readValue(objectMapper.convertValue(result.getClass(), JsonNode.class));
    }

    @Test
    @DisplayName("Should return null if merge data fails")
    void mergeDataCase2() throws IOException {
        String createdObject = "fake-object-string";

        ObjectReader objectReader = mock(ObjectReader.class);
        when(objectReader.readValue(objectMapper.convertValue(Object.class, JsonNode.class))).thenThrow(RuntimeException.class);

        when(objectMapper.readerForUpdating(any())).thenReturn(objectReader);

        String firstObject = "fake";
        String secondObject = "fake";

        Object result = dataHandler.mergeData(firstObject, secondObject);

        assertThat(result).isEqualTo(null);
    }
}
