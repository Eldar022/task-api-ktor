package com.example
import kotlinx.serialization.Serializable
@Serializable data class Task(val id: Int, val title: String, val completed: Boolean = false)
@Serializable data class CreateTaskRequest(val title: String, val completed: Boolean = false)
@Serializable data class ErrorResponse(val error: String)
