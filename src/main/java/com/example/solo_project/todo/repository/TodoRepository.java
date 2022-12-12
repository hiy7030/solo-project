package com.example.solo_project.todo.repository;

import com.example.solo_project.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByTodoOrder(int todoOrder);
}
