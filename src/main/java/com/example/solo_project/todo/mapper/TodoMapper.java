package com.example.solo_project.todo.mapper;

import com.example.solo_project.todo.dto.TodoDto;
import com.example.solo_project.todo.entity.Todo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    // post -> entity
    Todo TodoPostDtoToTodO(TodoDto.Post todoPostDto);
    // patch -> entity
    Todo TodoPatchDtoToTodO(TodoDto.Patch todoPatchDto);
    // entity -> response
    default TodoDto.Response TodoToTodoResponseDto(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDto.Response response = TodoDto.Response
                .builder()
                .todoId(todo.getTodoId())
                .title(todo.getTitle())
                .todoOrder(todo.getTodoOrder())
                .completed(todo.isCompleted())
                .url("http://localhost:8080/todos/" + todo.getTodoId())
                .build();

        return response;

    }
    // List<Todo> -> response
    List<TodoDto.Response> TodoListToTodoResponseDtos(List<Todo> todoList);
}
