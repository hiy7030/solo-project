package com.example.solo_project.todo.service;

import com.example.solo_project.exception.BusinessLoginException;
import com.example.solo_project.exception.ExceptionCode;
import com.example.solo_project.todo.entity.Todo;
import com.example.solo_project.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TodoService { // 검증 메서드 구현
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // todo create : request - ToDo instance, response - save Todo instance
    public Todo createTodo(Todo todo) {
        // 이미 등록된 회원인지 검증 필요
        verifyExistTodoOrder(todo.getTodoOrder());

        Todo saveTodo = todoRepository.save(todo);
        return saveTodo;
    }
    // todo update : request - ToDo instance, response - save Todo instance
    public Todo updateTodo(Todo todo) {
        // 이미 등록된 회원 찾는 검증 로직 필요
        Todo findTodo = findVerifiedTodo(todo.getTodoId());
        // 변경 가능한 것은 title, completed
        Optional.ofNullable(todo.getTitle())
                .ifPresent(title -> findTodo.setTitle(title));
        Optional.ofNullable(todo.getTodoOrder())
                        .ifPresent(todoOrder -> findTodo.setTodoOrder(todoOrder));

        Optional.ofNullable(todo.isCompleted())
                .ifPresent(completed -> findTodo.setCompleted(completed));

        todoRepository.save(findTodo);
        return findTodo;
    }
    // todo find one : request - TodoId, response - find Todo instance
    public Todo findTodo(long todoId) {
        // repository에서 todoId로 정보를 찾아온 뒤
        return findVerifiedTodo(todoId);
    }
    // todo find all : request - page, size, response - page<ToDO>
    public Page<Todo> findTodos(int page, int size) {
        // findAll(Pageable) -> pageable =  PageRequest.of(page, size, sort)
        Pageable pageable = PageRequest.of(page-1, size, Sort.by("todoId").descending());
        Page<Todo> todoPage = todoRepository.findAll(pageable);

        return todoPage;
    }
    // todo delete one : request - TodoId
    public void deleteTodo(long todoId) {
        // 회원 찾기
        Todo findTodo = findVerifiedTodo(todoId);
        // 삭제
        todoRepository.delete(findTodo);
    }
    // todo delete all : request - ?
    public void deleteTodos() {
        //전부 삭제 completed = true 인 것만
        List<Todo> todoList = todoRepository.findAll();
        todoList.stream()
                .filter(todo -> todo.isCompleted() == true)
                .forEach(todo -> todoRepository.deleteById(todo.getTodoId()));
    }

    // 존재 유무 검증
    private void verifyExistTodoOrder(int todoOrder) {
        Optional<Todo> optionalTodo = todoRepository.findByTodoOrder(todoOrder);
        if(optionalTodo.isPresent()) {
            throw new BusinessLoginException(ExceptionCode.TODO_EXIST);
        }
    }
    // 존재 유무 확인 후 리턴
    private Todo findVerifiedTodo(long todoId) {
        Optional<Todo> optionalTodo = todoRepository.findById(todoId);
        Todo todo = optionalTodo.orElseThrow(
                () -> new BusinessLoginException(ExceptionCode.TODO_NOT_FOUND)
        );

        return todo;
    }
}
