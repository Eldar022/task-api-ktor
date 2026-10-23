package com.example
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
class TaskApiTest {
    @Test fun `GET tasks returns JSON`() = testApplication {
        application { module() }
        val response = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.headers[HttpHeaders.ContentType].orEmpty().contains("application/json"))
        assertTrue(response.bodyAsText().startsWith("["))
    }
    @Test fun `POST creates task`() = testApplication {
        application { module() }
        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Study Ktor","completed":false}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Study Ktor"))
    }
    @Test fun `GET missing task returns 404`() = testApplication {
        application { module() }
        assertEquals(HttpStatusCode.NotFound, client.get("/tasks/999").status)
    }
    @Test fun `invalid query returns 400`() = testApplication {
        application { module() }
        assertEquals(HttpStatusCode.BadRequest, client.get("/tasks?completed=maybe").status)
    }
    @Test fun `DELETE missing task returns 404`() = testApplication {
        application { module() }
        assertEquals(HttpStatusCode.NotFound, client.delete("/tasks/999").status)
    }
}
