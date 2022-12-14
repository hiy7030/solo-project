package com.example.solo_project.todo.controller;

import com.example.solo_project.dto.MultiResponseDto;
import com.example.solo_project.dto.SingleResponseDto;
import com.example.solo_project.response.PageInfo;
import com.example.solo_project.todo.dto.TodoDto;
import com.example.solo_project.todo.entity.Todo;
import com.example.solo_project.todo.mapper.TodoMapper;
import com.example.solo_project.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@CrossOrigin("https://todobackend.com/")
@RestController
@RequestMapping("/todos")
@Validated
public class TodoController {
    private final TodoService todoService;
    private final TodoMapper mapper;

    public TodoController(TodoService todoService, TodoMapper mapper) {
        this.todoService = todoService;
        this.mapper = mapper;
    }

    private final static String TODO_DEFAULT_URI = "/todos";

    // 할 일 등록 - request : postDto, response - httpHeader(uri)
    @PostMapping
    public ResponseEntity postTodo(@Valid @RequestBody TodoDto.Post todoPostDto) {
        // 비즈니스 계층에 Dto 넘기고
        Todo todo = todoService.createTodo(mapper.TodoPostDtoToTodO(todoPostDto));
        // 받아온 Id로 uri 만들어서 헤더에 넣기
        URI location = UriComponentsBuilder.newInstance()
                .path(TODO_DEFAULT_URI + "/{todo-id}")
                .buildAndExpand(todo.getTodoId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    // 할 일 수정 - request: patahDto, response - ToDo instance
    @PatchMapping("/{todo-id}")
    public ResponseEntity patchTodo(@PathVariable("todo-id") @Positive long todoId,
                                    @Valid @RequestBody TodoDto.Patch toDoPatchDto) {
        Todo todo = mapper.TodoPatchDtoToTodO(toDoPatchDto);
        todo.setTodoId(todoId);

        Todo updateTodo = todoService.updateTodo(todo);
        TodoDto.Response response = mapper.TodoToTodoResponseDto(updateTodo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 할 일 조회 - request : ToDoId, response - ToDo instance
    @GetMapping("/{todo-id}")
    public ResponseEntity getTodo(@PathVariable("todo-id") @Positive long todoId) {
        Todo todo = todoService.findTodo(todoId);
        TodoDto.Response response = mapper.TodoToTodoResponseDto(todo);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
//    // 할 일 목록 조회 - request : page, size, response - page<Todo>
//    @GetMapping
//    public ResponseEntity getTodos(@Positive @RequestParam int page,
//                                   @Positive @RequestParam int size) {
//        // pageInfo
//        Page<Todo> todoPage = todoService.findTodos(page, size);
//        PageInfo pageInfo = new PageInfo(page, size, todoPage.getTotalPages(), (int) todoPage.getTotalElements());
//        // todoList
//        List<Todo> todoList = todoPage.getContent();
//        List<TodoDto.Response> response = mapper.TodoListToTodoResponseDtos(todoList);
//        return new ResponseEntity<>(new MultiResponseDto<>(response, pageInfo), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity getTodos() {
        List<Todo> todoList = todoService.findTodos();
        List<TodoDto.Response> responses = mapper.TodoListToTodoResponseDtos(todoList);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    // 할 일 삭제 - request : ToDoId;
    @DeleteMapping("/{todo-id}")
    public ResponseEntity deleteTodo(@PathVariable("todo-id") @Positive long todoId) {
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    // 할일 전체 삭제
    @DeleteMapping
    public ResponseEntity deleteTodos() {
        todoService.deleteTodos();
        return new ResponseEntity(HttpStatus.RESET_CONTENT);
    }
}
