package com.example.todo.todo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TodoServiceTest {
    private final TodoService service = new TodoService();

    @Test
    void createsAndCompletesATodo() {
        Todo created = service.create("Write tests", "Keep them focused", Priority.HIGH, LocalDate.of(2026, 10, 1));
        Todo completed = service.updateDone(created.id(), true);

        assertThat(completed.done()).isTrue();
        assertThat(completed.priority()).isEqualTo(Priority.HIGH);
        assertThat(completed.dueDate()).isEqualTo(LocalDate.of(2026, 10, 1));
        assertThat(service.findAll()).contains(completed);
    }

    @Test
    void startsWithThreeSampleTodos() {
        assertThat(service.findAll())
                .hasSize(3)
                .extracting(Todo::title)
                .containsExactly("Buy groceries", "Call the dentist", "Finish monthly report");
    }

    @Test
    void trimsNewTodoText() {
        Todo created = service.create("  Write tests  ", "  Keep them focused  ", Priority.MEDIUM, null);

        assertThat(created.title()).isEqualTo("Write tests");
        assertThat(created.description()).isEqualTo("Keep them focused");
        assertThat(created.done()).isFalse();
        assertThat(created.priority()).isEqualTo(Priority.MEDIUM);
        assertThat(created.dueDate()).isNull();
    }

    @Test
    void rejectsAnUnknownTodoId() {
        assertThatThrownBy(() -> service.updateDone(999, true))
                .isInstanceOf(TodoNotFoundException.class)
                .hasMessage("Todo 999 was not found");
    }
}
