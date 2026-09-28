package com.example.todo.todo;

import jakarta.validation.constraints.NotNull;

public record UpdateDoneRequest(@NotNull Boolean done) {
}
