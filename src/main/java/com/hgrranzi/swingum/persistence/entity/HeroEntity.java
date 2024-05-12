package com.hgrranzi.swingum.persistence.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.IOException;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeroEntity {

    @Setter
    @Min(1)
    private Integer id;

    @NotBlank
    @Size(min = 4, max = 16)
    private String name;

    @Builder.Default
    @PastOrPresent
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
