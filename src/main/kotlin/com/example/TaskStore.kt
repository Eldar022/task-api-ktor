package com.example
import java.util.concurrent.atomic.AtomicInteger
class TaskStore {
    private val nextId = AtomicInteger(1)
    private val tasks = linkedMapOf<Int, Task>()
    @Synchronized fun all(completed: Boolean? = null): List<Task> =
        tasks.values.filter { completed == null || it.completed == completed }
    @Synchronized fun find(id: Int): Task? = tasks[id]
    @Synchronized fun create(request: CreateTaskRequest): Task {
        val task = Task(nextId.getAndIncrement(), request.title.trim(), request.completed)
        tasks[task.id] = task
        return task
    }
    @Synchronized fun delete(id: Int): Boolean = tasks.remove(id) != null
}
