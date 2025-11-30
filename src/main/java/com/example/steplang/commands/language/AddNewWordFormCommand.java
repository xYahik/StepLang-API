package com.example.steplang.commands.language;

import com.example.steplang.utils.enums.language.WordGender;
import com.example.steplang.utils.enums.language.WordNumber;
import com.example.steplang.utils.enums.language.WordTense;
import com.example.steplang.utils.enums.language.WordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddNewWordFormCommand {
    private Long languageId;

    @NotNull(message = "'wordId' is required")
    private final Long wordId;

    @NotBlank(message = "'form' is required")
    private final String form;

    private final Integer wordPerson;
    private final WordNumber wordNumber;
    private final WordGender wordGender;
    private final WordTense wordTense;
}
