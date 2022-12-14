package com.example.solo_project.todo.controller;

import com.example.solo_project.todo.dto.TodoDto;
import com.example.solo_project.todo.entity.Todo;
import com.example.solo_project.todo.mapper.TodoMapper;
import com.example.solo_project.todo.service.TodoService;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TodoService todoService;

    @Autowired
    private TodoMapper mapper;

    @Test
    void postTodoTest() throws Exception {
        //given -> 전제 조건 : TodoPostDto
        TodoDto.Post post = new TodoDto.Post();
        post.setTitle("운동하기");
        post.setTodoOrder(1);

        Todo todo = mapper.TodoPostDtoToTodO(post);

        String content = gson.toJson(post);
        // service 연동 끊기
        given(todoService.createTodo(Mockito.any(Todo.class))).willReturn(todo);

        //when -> 수행할 동작 postToDo()
        ResultActions actions =
                mockMvc.perform(post("/todos")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        //then -> 검증 HttpStatus, header의 담긴 uri
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/todos/"))));
    }

    @Test
    void patchTodoTest() throws Exception {
        //given -> 전제조건 TodoPatchDto
        TodoDto.Patch patch = new TodoDto.Patch();
        patch.setTitle("운동하기");
        patch.setTodoOrder(1);
        patch.setCompleted(true);

        Todo todo = mapper.TodoPatchDtoToTodO(patch);
        todo.setTodoId(1L);

        String content = gson.toJson(patch);

        given(todoService.updateTodo(Mockito.any(Todo.class))).willReturn(todo);

        //when -> 수행할 동작 patchTodo()
        ResultActions actions =
                mockMvc.perform(patch("/todos/{todo-id}", todo.getTodoId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        //then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(patch.getTitle()))
                .andExpect(jsonPath("$.completed").value(patch.isCompleted()))
                .andExpect(jsonPath("$.todoOrder").value(patch.getTodoOrder()))
                .andExpect(jsonPath("$.url").value("http://localhost:8080/todos/" + todo.getTodoId()))
                .andReturn();

    }

    @Test
    void getTodoTest() throws Exception {
        //given 전제조건 : todo instance?
        TodoDto.Post post = new TodoDto.Post();
        post.setTitle("운동하기");
        post.setTodoOrder(1);

        Todo todo = mapper.TodoPostDtoToTodO(post);
        todo.setTodoId(1L);

        String content = gson.toJson(post);

        given(todoService.findTodo(Mockito.anyLong())).willReturn(todo);

        //when
        ResultActions actions =
                mockMvc.perform(get("/todos/{todo-id}", todo.getTodoId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));
        //then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.todoOrder").value(post.getTodoOrder()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andExpect(jsonPath("$.url").value("http://localhost:8080/todos/" + todo.getTodoId()))
                .andReturn();
    }

//    @Test
//    void getTodosTest() throws Exception {
//        //given -> page, size
//        Todo todo1 = new Todo("운동하기", 1, false);
//        todo1.setTodoId(1L);
//
//        Todo todo2 = new Todo("공부하기", 2, true);
//        todo2.setTodoId(2L);
//
//        //페이지네이션
//        Page<Todo> todoPage = new PageImpl<>(List.of(todo1, todo2),
//                PageRequest.of(0, 10, Sort.by("todoId").descending()),2);
//
//        given(todoService.findTodos(Mockito.anyInt(), Mockito.anyInt())).willReturn(todoPage);
//
//        String page = "1";
//        String size = "10";
//        MultiValueMap<String, String> pages = new LinkedMultiValueMap<>();
//        pages.add("page", page);
//        pages.add("size", size);
//
//        //when
//        ResultActions actions =
//                mockMvc.perform(get("/todos")
//                                .params(pages)
//                        .accept(MediaType.APPLICATION_JSON));
//        //then
//        MvcResult result =
//                actions.andExpect(status().isOk())
//                        .andExpect(jsonPath("$.data").isArray())
//                        .andReturn();
//
//        List list = JsonPath.parse(result.getResponse().getContentAsString()).read("$.data");
//
//        assertThat(list.size(), is(2));
//    }

    @Test
    void getTodosTest() throws Exception {
        //given -> 객체 두개
        Todo todo1 = new Todo("운동하기", 1, false);
        todo1.setTodoId(1L);

        Todo todo2 = new Todo("말하기", 2, false);
        todo2.setTodoId(2L);

        List<Todo> todoList = new ArrayList<>(List.of(todo1, todo2));

        given(todoService.findTodos()).willReturn(todoList);
        //when
        ResultActions actions =
                mockMvc.perform(get("/todos")
                        .accept(MediaType.APPLICATION_JSON));
        //then
        MvcResult result = actions.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
    }

    @Test
    void deleteTodoTest() throws Exception {
        //given postDto
        Todo todo = new Todo();
        todo.setTodoId(1L);
        todo.setTodoOrder(1);
        todo.setTitle("운동하기");

        doNothing().when(todoService).deleteTodo(Mockito.anyLong());
        //when
        ResultActions actions =
                mockMvc.perform(delete("/todos/{todoId}", todo.getTodoId())
                        .accept(MediaType.APPLICATION_JSON));
        //then
        MvcResult result = actions.andExpect(status().isNoContent()).andReturn();
    }

    @Test
    void deleteTodosTest() throws Exception {
        //given
        Todo todo1 = new Todo("운동하기", 1, false);
        todo1.setTodoId(1L);

        Todo todo2 = new Todo("공부하기", 2, true);
        todo2.setTodoId(2L);

        doNothing().when(todoService).deleteTodos();
        //when
        ResultActions actions =
                mockMvc.perform(delete("/todos")
                        .accept(MediaType.APPLICATION_JSON));
        //then
        MvcResult result = actions.andExpect(status().isResetContent()).andReturn();


    }
}
