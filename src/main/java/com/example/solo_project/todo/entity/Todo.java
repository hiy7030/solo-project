package com.example.solo_project.todo.entity;

import com.example.solo_project.advice.audit.Audit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Todo extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @Column(nullable = false, length = 30)
    private String title;

    //우선순위- 고유값? 중복허용??
    @Column(unique = true)
    private int todoOrder;

    @Column(nullable = false)
    private boolean completed = false;

    public Todo(String title, int todoOrder, boolean completed) {
        this.title = title;
        this.todoOrder = todoOrder;
        this.completed = completed;
    }
}
