package com.epam.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JPAJacksonObjectMapper<T> {
    private ObjectMapper objectMapper;

    public JPAJacksonObjectMapper() {
        Hibernate5Module module = new Hibernate5Module();
        module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()).registerModule(module);
    }

    public File serializeToFile(T t, String filePath) throws IOException {
        File file = new File(filePath);
        objectMapper.writeValue(file, t);
        return file;
    }

    public T deserializeFromFile(File file, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(file, typeReference);
    }
}
