package com.example.steplang.model.task.course;

import com.example.steplang.model.course.CourseActionChooseWordWithImage;
import com.example.steplang.model.task.TaskDataBase;
import lombok.Data;

import java.util.List;

@Data
public class ChooseWordWithImageData extends TaskDataBase {
    private CourseActionChooseWordWithImage actionData;
}