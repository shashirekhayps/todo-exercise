import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Todo {
  id: number;
  title: string;
  description: string;
  done: boolean;
  priority: Priority;
  dueDate: string | null;
}

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH';
export type CreateTodo = Pick<Todo, 'title' | 'description' | 'priority' | 'dueDate'>;

@Injectable({ providedIn: 'root' })
export class TodoService {
  private readonly apiUrl = '/api/todos';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Todo[]> {
    return this.http.get<Todo[]>(this.apiUrl);
  }

  create(todo: CreateTodo): Observable<Todo> {
    return this.http.post<Todo>(this.apiUrl, todo);
  }

  updateDone(id: number, done: boolean): Observable<Todo> {
    return this.http.patch<Todo>(`${this.apiUrl}/${id}/done`, { done });
  }
}
