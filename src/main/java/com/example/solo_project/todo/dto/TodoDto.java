package com.example.solo_project.todo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class TodoDto {
    @Getter
    @Setter
    public static class Post { // 유효성 검사
        @NotNull
        private String title;

        @Positive
        private int todoOrder;

    }

    @Getter
    @Setter
    public static class Patch { // 유효성 검사
        @NotNull
        private String title;

        @Positive
        private int todoOrder;

        private boolean completed;
    }

    @Getter
    @Setter
    @Builder
    public static class Response {
        private long todoId;
        private String title;
        private int todoOrder;
        private boolean completed;
        private String url;
    }
}
