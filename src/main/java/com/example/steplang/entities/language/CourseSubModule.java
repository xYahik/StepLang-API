package com.example.steplang.entities.language;

import com.example.steplang.model.course.CourseActionBase;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSubModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer subModuleId;

    private String subModuleName;

    @ManyToOne
    private CourseModule module;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<CourseActionBase> actions;

    public CourseSubModule(Integer subModuleId, String subModuleName){
        actions = new ArrayList<>();
        this.subModuleId = subModuleId;
        this.subModuleName = subModuleName;
    }

    public void addAction(CourseActionBase action){
        actions.add(action);
    }
    public void removeAction(String actionId){
        actions.removeIf(action -> action.getId().equals(actionId));
    }
}
