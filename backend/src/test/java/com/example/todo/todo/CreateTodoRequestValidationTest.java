package com.example.todo.todo;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTodoRequestValidationTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void rejectsAPastDueDate() {
        CreateTodoRequest request = new CreateTodoRequest(
                "Buy groceries",
                "Pick up milk",
                Priority.MEDIUM,
                LocalDate.now().minusDays(1)
        );

        assertThat(validator.validate(request))
                .extracting(violation -> violation.getPropertyPath().toString())
                .containsExactly("dueDate");
    }

    @Test
    void acceptsNoDueDate() {
        CreateTodoRequest request = new CreateTodoRequest(
                "Buy groceries",
                "Pick up milk",
                Priority.MEDIUM,
                null
        );

        assertThat(validator.validate(request)).isEmpty();
    }
}
