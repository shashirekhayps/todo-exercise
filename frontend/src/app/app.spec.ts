import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { App } from './app';

describe('App', () => {
  it('renders the page heading', async () => {
    await TestBed.configureTestingModule({ imports: [App], providers: [provideHttpClient()] }).compileComponents();
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Todo List');
  });
});
