package com.example.solo_project.todo.repository;

import com.example.solo_project.exception.BusinessLoginException;
import com.example.solo_project.exception.ExceptionCode;
import com.example.solo_project.todo.entity.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static java.lang.Boolean.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

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
        Todo findTodo = optionalTodo.get();

        //then -> 객체와 찾아온 객체 비교
        assertTrue(todo.getTitle().equals(findTodo.getTitle()));
        assertThat(todo.getTodoOrder(), is(findTodo.getTodoOrder()));
        assertThat(todo.isCompleted(), is(findTodo.isCompleted()));
    }

    // 투두 조회
    @Test
    void findByIdTodoTest() {
        // given -> 객체, db에 저장
        Todo todo = new Todo("운동하기", 1, false);
        todo.setTodoId(1L);

        todoRepository.save(todo);
        // when -> findById()
        Optional<Todo> optionalTodo = todoRepository.findById(todo.getTodoId());
        Todo findTodo = optionalTodo.get();
        // then
        assertTrue(todo.getTitle().equals(findTodo.getTitle()));
        assertThat(todo.getTodoOrder(), is(findTodo.getTodoOrder()));
        assertThat(todo.isCompleted(), is(findTodo.isCompleted()));
    }

    // 투두 전체 조희
    @Test
    void findAllTodoTest() {
        //given
        Todo todo1 = new Todo("운동하기", 1, false);
        todo1.setTodoId(1L);

        Todo todo2 = new Todo("하기", 2, false);
        todo2.setTodoId(2L);

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        //when
        List<Todo> todoList = todoRepository.findAll();
        //then
        assertThat(todoList.size(), is(2));
    }

    // 투두 삭제
    @Test
    void deleteTodoTest() {
        //given -> 삭제할 객체
        Todo todo1 = new Todo("운동하기", 1, false);
        todo1.setTodoId(1L);

        todoRepository.save(todo1);
        //when
        todoRepository.delete(todo1);
        //then findByid -> exception(To do not Exist)을 던져야함
        Optional<Todo> findOptionalTodo = todoRepository.findById(todo1.getTodoId());

        assertThat(findOptionalTodo, is(Optional.empty()));


    }

//    // 투두 true만 삭제 -> service 테스트가 아닌가?
//    @Test
//    void deleteTodosTest() {
//        //given
//        Todo todo1 = new Todo("뭐하기", 1, false);
//        todo1.setTodoId(1L);
//        Todo todo2 = new Todo("이거하기", 3, true);
//        todo2.setTodoId(2L);
//        Todo todo3 = new Todo("저거하기", 2, false);
//        todo3.setTodoId(3L);
//
//        todoRepository.save(todo1);
//        todoRepository.save(todo2);
//        todoRepository.save(todo3);
//        //when
//        List<Todo> todoList = todoRepository.findAll();
//        todoList.stream().filter(todo -> valueOf(todo.isCompleted()).equals(TRUE))
//                .forEach(todo -> todoRepository.deleteById(todo.getTodoId()));
//        //then
//        List<Todo> assertList = todoRepository.findAll();
//        assertList.stream().forEach(todo -> assertThat(valueOf(todo.isCompleted()), is(FALSE)));
//    }
}
