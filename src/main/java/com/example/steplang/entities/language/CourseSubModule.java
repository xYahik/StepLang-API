package com.example.steplang.entities.language;

import com.example.steplang.model.course.CourseActionBase;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseSubModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer subModuleId;

    private String subModuleName;

    @ManyToOne
    private CourseModule module;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<CourseActionBase> tasks;
}
