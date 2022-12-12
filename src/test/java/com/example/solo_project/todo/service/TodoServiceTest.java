package com.example.solo_project.todo.service;

import com.example.solo_project.exception.BusinessLoginException;
import com.example.solo_project.todo.entity.Todo;
import com.example.solo_project.todo.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @InjectMocks // @Mock 애너테이션이 붙은 가짜객체를 주입할 수 있는 애너테이션
    private TodoService todoService;

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



}
