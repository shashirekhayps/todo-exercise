package com.example.todo.todo;

import java.time.LocalDate;

public record Todo(
        long id,
        String title,
        String description,
        boolean done,
        Priority priority,
        LocalDate dueDate
) {
}
