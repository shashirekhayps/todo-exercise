package com.example.todo.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {
    private final ConcurrentMap<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong();

    public TodoService() {
        create("Buy groceries", "Pick up milk, vegetables, and coffee.", Priority.MEDIUM, LocalDate.of(2026, 10, 1));
        create("Call the dentist", "Schedule a routine check-up.", Priority.LOW, LocalDate.of(2026, 10, 8));
        Todo completed = create("Finish monthly report", "Review the figures and send the final version.", Priority.HIGH, LocalDate.of(2026, 9, 30));
        updateDone(completed.id(), true);
    }

    public List<Todo> findAll() {
        return todos.values().stream().sorted((left, right) -> Long.compare(left.id(), right.id())).toList();
    }

    public Todo create(String title, String description, Priority priority, LocalDate dueDate) {
        long id = nextId.incrementAndGet();
        Todo todo = new Todo(id, title.trim(), description.trim(), false, priority, dueDate);
        todos.put(id, todo);
        return todo;
    }

    public Todo updateDone(long id, boolean done) {
        return todos.compute(id, (key, existing) -> {
            if (existing == null) {
                throw new TodoNotFoundException(id);
            }
            return new Todo(existing.id(), existing.title(), existing.description(), done, existing.priority(), existing.dueDate());
        });
    }
}
