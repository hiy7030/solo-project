package com.example.solo_project.todo.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TodoDto {
    @Getter
    @Setter
    public static class Post { // 유효성 검사
        @NotNull
        @NotBlank
        private String title;

        @Positive
        @NotNull
        private int todoOrder;

    }

    @Getter
    @Setter
    public static class Patch { // 유효성 검사
        @NotNull
        @NotBlank
        private String title;

        @Positive
        @NotNull
        private int todoOrder;

        private boolean completed;
    }

    @Getter
    @Setter
    public static class Response {
        private long todoId;
        private String title;
        private int todoOrder;
        private boolean completed;
    }
}
