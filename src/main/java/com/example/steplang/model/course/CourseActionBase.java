package com.example.steplang.model.course;

import com.example.steplang.utils.enums.CourseActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = CourseActionInformation.class
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
    @JsonIgnore
    public final CourseActionType getActionType() {
        if (this instanceof CourseActionChooseWordWithImage) return CourseActionType.CHOOSE_WORD_WITH_IMAGE;
        if (this instanceof CourseActionInformation) return CourseActionType.INFORMATION;
        throw new IllegalStateException("Unknown CourseActionType: " + getClass());
    }
}
