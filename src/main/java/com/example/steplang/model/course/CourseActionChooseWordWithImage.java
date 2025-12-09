package com.example.steplang.model.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseActionChooseWordWithImage extends CourseActionBase{

    private Long chosenWordId;
    private List<Long> extraWordsId;
}
