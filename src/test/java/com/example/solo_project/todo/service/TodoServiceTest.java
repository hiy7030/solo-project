package com.example.solo_project.todo.service;

import com.example.solo_project.exception.BusinessLoginException;
import com.example.solo_project.todo.entity.Todo;
import com.example.solo_project.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.Boolean.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks // @Mock 애너테이션이 붙은 가짜객체를 주입할 수 있는 애너테이션
    private TodoService todoService;

    @BeforeEach
    void init() {
        todoRepository.deleteAll();
    }

    @Test
    void createTodoTest() { // verifyExistTodoOrder() 테스트
        //given -> 객체, repository 분리
        Todo todo = new Todo("운동하기", 1, false);
        todo.setTodoId(1L);

        given(todoRepository.findByTodoOrder(Mockito.anyInt())).willReturn(Optional.of(todo));
        //when
        //then -> 예외를 던지느냐를 검증해야함! 존재하니까 예외를 던져!
        assertThrows(BusinessLoginException.class, ()-> todoService.createTodo(todo));
    }

    // findVerifiedTodo() 사용 시 예외를 던지는 테스트
    @Test
    void findVerifiedTodoTest() {
        //given -> 객체
        Todo todo = new Todo("운동하기", 1, false);
        todo.setTodoId(1L);

        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());
        //when
        //then

        assertThrows(BusinessLoginException.class, () -> todoService.findVerifiedTodo(todo.getTodoId()));
    }

    // 투두 true만 삭제
    @Test
    void deleteTodosTest() {
        //given
        Todo todo1 = new Todo("뭐하기", 1, false);
        todo1.setTodoId(1L);
        Todo todo2 = new Todo("이거하기", 3, true);
        todo2.setTodoId(2L);
        Todo todo3 = new Todo("저거하기", 2, false);
        todo3.setTodoId(3L);

        List<Todo> todoList = new ArrayList<>(List.of(todo1, todo2, todo3));

        List<Todo> testList = todoList.stream()
                .filter(todo -> valueOf(todo.isCompleted()).equals(FALSE))
                .collect(Collectors.toList());

        //when
        //then
        assertThat(testList.size(), is(2));
        testList.stream().forEach(todo -> assertThat(valueOf(todo.isCompleted()), is(FALSE)));
    }
}
