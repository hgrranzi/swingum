package com.hgrranzi.swingum.persistence.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeroEntity {

    @Setter
    private Integer id;

    @NotBlank
    private String name;

    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Builder.Default
    @JsonRawValue
    @JsonDeserialize(using = RawJsonDeserializer.class)
    private String serializedData = null;

}

class RawJsonDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return p.readValueAsTree().toString();
    }
}
