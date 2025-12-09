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
        @JsonSubTypes.Type(value = CourseActionInformation.class, name = "INFORMATION"),
        @JsonSubTypes.Type(value = CourseActionChooseWordWithImage.class, name = "CHOOSE_WORD_WITH_IMAGE"),
})
public abstract class CourseActionBase {
    private final String id = UUID.randomUUID().toString();
    public String getId() {
        return id;
    }
}
