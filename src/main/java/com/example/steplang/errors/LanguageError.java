package com.example.steplang.errors;

import com.fasterxml.jackson.annotation.JsonValue;

public enum LanguageError {
    MISSING_KEY,
    LANGUAGE_ID_NOT_FOUND,
    LANGUAGE_AND_WORD_ID_NOT_FOUND,
    WORD_ID_NOT_FOUND,
    CATEGORY_ID_NOT_FOUND,
    ALREADY_EXISTS
}
