package com.example.steplang.model.course;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CourseActionInformation.class, name = "Information"),
        @JsonSubTypes.Type(value = CourseActionWordRepetition.class, name = "WordRepetition")
})
public abstract class CourseActionBase {
    private String id = UUID.randomUUID().toString();
    public String getId() {
        return id;
    }
}
