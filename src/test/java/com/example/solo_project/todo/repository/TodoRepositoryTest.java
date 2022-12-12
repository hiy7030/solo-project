package com.example.solo_project.todo.repository;

import com.example.solo_project.exception.BusinessLoginException;
import com.example.solo_project.exception.ExceptionCode;
import com.example.solo_project.todo.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    void saveTodoTest() {
        //given -> 저장할 객체
        Todo todo = new Todo("운동하기", 1, false);
        todo.setTodoId(1L);

        //when -> save
        Todo saveTodo = todoRepository.save(todo);

        //then -> 저장한 객체와 저장된 객체 비교
        assertNotNull(saveTodo);
        assertTrue(todo.getTitle().equals(saveTodo.getTitle()));
        assertThat(todo.getTodoOrder(), is(saveTodo.getTodoOrder()));
        assertThat(todo.isCompleted(), is(saveTodo.isCompleted()));

    }

    @Test
    void findByTodoOrderTest() {
        //given -> 객체
        Todo todo = new Todo("운동하기", 1, false);
        todo.setTodoId(1L);

        todoRepository.save(todo);

        //when -> findByTodoOrder
        Optional<Todo> optionalTodo = todoRepository.findByTodoOrder(todo.getTodoOrder());
        Todo findTodo = optionalTodo.orElseThrow(
                () -> new BusinessLoginException(ExceptionCode.TODO_NOT_FOUND)
        );

        //then -> 객체와 찾아온 객체 비교
        assertTrue(todo.getTitle().equals(findTodo.getTitle()));
        assertThat(todo.getTodoOrder(), is(findTodo.getTodoOrder()));
        assertThat(todo.isCompleted(), is(findTodo.isCompleted()));
    }
}
