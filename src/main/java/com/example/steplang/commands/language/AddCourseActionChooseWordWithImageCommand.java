package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AddCourseActionChooseWordWithImageCommand extends AddCourseActionCommand{
    @NotNull(message = "'chosenWordId' is required")
    private Long chosenWordId;
    @NotNull(message = "'extraWordsId' is required")
    private List<Long> extraWordsId;
}
