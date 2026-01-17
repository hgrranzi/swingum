package com.hgrranzi.swingum.persistence.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;

import java.io.IOException;
import java.time.LocalDateTime;

public class HeroEntity {

    private Integer id;

    @NotBlank
    private String name;

    private LocalDateTime lastUpdated = LocalDateTime.now();

    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String serializedData = null;

    // Constructors
    public HeroEntity() {
    }

    public HeroEntity(Integer id, String name, LocalDateTime lastUpdated, String serializedData) {
        this.id = id;
        this.name = name;
        this.lastUpdated = lastUpdated;
        this.serializedData = serializedData;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public String getSerializedData() {
        return serializedData;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setSerializedData(String serializedData) {
        this.serializedData = serializedData;
    }

    // Builder
    public static HeroEntityBuilder builder() {
        return new HeroEntityBuilder();
    }

    public static class HeroEntityBuilder {
        private Integer id;
        private String name;
        private LocalDateTime lastUpdated = LocalDateTime.now();
        private String serializedData = null;

        public HeroEntityBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public HeroEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public HeroEntityBuilder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public HeroEntityBuilder serializedData(String serializedData) {
            this.serializedData = serializedData;
            return this;
        }

        public HeroEntity build() {
            return new HeroEntity(id, name, lastUpdated, serializedData);
        }
    }

}

class RawJsonDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return p.readValueAsTree().toString();
    }
}
