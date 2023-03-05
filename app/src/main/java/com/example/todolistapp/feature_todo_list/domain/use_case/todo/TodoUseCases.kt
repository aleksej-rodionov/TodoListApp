package com.example.todolistapp.feature_todo_list.domain.use_case.todo

data class TodoUseCases(
    val insertTodo: InsertTodo,
    val updateTodo: UpdateTodo,
    val deleteTodo: DeleteTodo,
    val getTodoById: GetTodoById,
    val getAllTodos: GetAllTodos
)