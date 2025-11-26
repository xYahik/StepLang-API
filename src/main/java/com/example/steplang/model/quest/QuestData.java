package com.example.steplang.model.quest;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        //Name - value of enum QuestActionType
        @JsonSubTypes.Type(value= QuestData_EarnExp.class, name="EARN_EXP"),
})
public class QuestData {
}
