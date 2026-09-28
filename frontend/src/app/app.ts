import { DatePipe } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { Priority, Todo, TodoService } from './todo.service';

type SortOption = 'created' | 'priority' | 'dueDate' | 'status';

@Component({
  selector: 'app-root',
  imports: [FormsModule, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly todos = signal<Todo[]>([]);
  protected readonly pendingCount = computed(() => this.todos().filter((todo) => !todo.done).length);
  protected readonly doneCount = computed(() => this.todos().filter((todo) => todo.done).length);
  protected readonly sortBy = signal<SortOption>('created');
  protected readonly sortedTodos = computed(() => {
    const priorityRank: Record<Priority, number> = { HIGH: 0, MEDIUM: 1, LOW: 2 };
    return [...this.todos()].sort((left, right) => {
      switch (this.sortBy()) {
        case 'priority':
          return priorityRank[left.priority] - priorityRank[right.priority] || left.id - right.id;
        case 'dueDate':
          return (left.dueDate ?? '9999-12-31').localeCompare(right.dueDate ?? '9999-12-31') || left.id - right.id;
        case 'status':
          return Number(left.done) - Number(right.done) || left.id - right.id;
        default:
          return left.id - right.id;
      }
    });
  });
  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly error = signal('');
  protected title = '';
  protected description = '';
  protected priority: Priority = 'MEDIUM';
  protected dueDate = '';

  constructor(private readonly todoService: TodoService) {}

  ngOnInit(): void {
    this.todoService.getAll().subscribe({
      next: (todos) => this.todos.set(todos),
      error: () => {
        this.error.set('Could not load todos. Is the API running?');
        this.loading.set(false);
      },
      complete: () => this.loading.set(false),
    });
  }

  protected createTodo(): void {
    const title = this.title.trim();
    const description = this.description.trim();
    if (!title || !description || this.saving()) return;

    this.saving.set(true);
    this.error.set('');
    this.todoService.create({ title, description, priority: this.priority, dueDate: this.dueDate || null })
      .pipe(finalize(() => this.saving.set(false))).subscribe({
      next: (todo) => {
        this.todos.update((todos) => [...todos, todo]);
        this.title = '';
        this.description = '';
        this.priority = 'MEDIUM';
        this.dueDate = '';
      },
      error: () => this.error.set('Could not create the todo.'),
    });
  }

  protected toggle(todo: Todo): void {
    this.error.set('');
    this.todoService.updateDone(todo.id, !todo.done).subscribe({
      next: (updated) => this.todos.update((todos) =>
        todos.map((item) => item.id === updated.id ? updated : item)),
      error: () => this.error.set('Could not update the todo.'),
    });
  }
}
