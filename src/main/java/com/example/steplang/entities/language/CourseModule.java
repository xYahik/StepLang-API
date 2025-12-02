package com.example.steplang.entities.language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CourseModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private Course course;

    private Integer moduleId;
    private String moduleName;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseSubModule> subModuleList;

    public CourseModule(Integer moduleId, String moduleName){
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    public void addSubModule(CourseSubModule subModule){
        subModule.setModule(this);
        subModuleList.add(subModule);

    }
    public void removeSubModule(CourseSubModule subModule){
        subModule.setModule(null);
        subModuleList.remove(subModule);
    }
}
