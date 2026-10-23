package com.example
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
fun Application.configureRouting(store: TaskStore = TaskStore()) {
    routing {
        get("/") {
            call.respond(mapOf("name" to "Task API", "endpoints" to listOf("GET /tasks", "GET /tasks/{id}", "POST /tasks", "DELETE /tasks/{id}")))
        }
        get("/tasks") {
            val raw = call.request.queryParameters["completed"]
            val completed = when {
                raw == null -> null
                raw.equals("true", true) -> true
                raw.equals("false", true) -> false
                else -> {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("Query parameter 'completed' must be true or false"))
                    return@get
                }
            }
            call.respond(HttpStatusCode.OK, store.all(completed))
        }
        get("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null || id <= 0) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Path parameter 'id' must be a positive integer")); return@get
            }
            val task = store.find(id)
            if (task == null) call.respond(HttpStatusCode.NotFound, ErrorResponse("Task with id $id was not found"))
            else call.respond(HttpStatusCode.OK, task)
        }
        post("/tasks") {
            val request = try { call.receive<CreateTaskRequest>() } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Request body must be valid JSON with a title")); return@post
            }
            if (request.title.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Task title must not be blank")); return@post
            }
            call.respond(HttpStatusCode.Created, store.create(request))
        }
        delete("/tasks/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null || id <= 0) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("Path parameter 'id' must be a positive integer")); return@delete
            }
            if (store.delete(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound, ErrorResponse("Task with id $id was not found"))
        }
    }
}
