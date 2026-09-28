package com.example.todo.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateTodoRequest(
        @NotBlank @Size(max = 100) String title,
        @NotBlank @Size(max = 500) String description,
        @NotNull Priority priority,
        LocalDate dueDate
) {
}
