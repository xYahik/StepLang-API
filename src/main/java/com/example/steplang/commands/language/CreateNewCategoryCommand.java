package com.example.steplang.commands.language;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateNewCategoryCommand {

    @NotBlank(message = "'name' is required")
    private String name;
}
