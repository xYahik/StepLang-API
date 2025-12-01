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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Language learningLanguageId;
    @ManyToOne
    private Language nativeLanguageId;

    private String name;

    @OneToMany(mappedBy = "course")
    List<CourseModule> modules;
}
