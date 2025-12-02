package com.example.steplang.entities.language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Language learningLanguage;
    @ManyToOne
    private Language nativeLanguage;

    private String name;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CourseModule> modules;

    public Course(String name, Language learningLanguage, Language nativeLanguage){
        this.name = name;
        this.learningLanguage = learningLanguage;
        this.nativeLanguage = nativeLanguage;
        this.modules = new ArrayList<>();
    }

    public void addModule(CourseModule module){
        module.setCourse(this);
        modules.add(module);
    }
    public void removeModule(CourseModule module){
        module.setCourse(null);
        modules.remove(module);
    }
}
