package ledger.http

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.ContentTransformationException
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.serialization.kotlinx.json.json
import ledger.domain.AccountId
import ledger.domain.TransferResult
import ledger.serialization.ErrorDto
import ledger.serialization.TransferRequestDto
import ledger.serialization.ledgerJson
import ledger.serialization.toDomain
import ledger.serialization.toDto
import ledger.serialization.toErrorDto

fun Application.ledgerModule(dependencies: LedgerDependencies) {
    install(ContentNegotiation) {
        json(ledgerJson())
    }

    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorDto("bad_request", cause.message ?: "bad request"),
            )
        }
        exception<ContentTransformationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorDto("bad_request", cause.message ?: "bad request"),
            )
        }
        exception<Throwable> { call, _ ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorDto("internal_error", "Internal server error"),
            )
        }
    }

    routing {
        get("/health") {
            call.respond(HealthDto("UP"))
        }

        get("/accounts") {
            call.respond(dependencies.accounts.findAll().map { it.toDto() })
        }

        get("/accounts/{id}") {
            val rawId = call.parameters["id"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorDto("bad_request", "account id is required"),
                )
            val accountId = runCatching { AccountId(rawId) }.getOrElse { error ->
                return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorDto("bad_request", error.message ?: "invalid account id"),
                )
            }
            val account = dependencies.accounts.findById(accountId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ErrorDto("account_not_found", "Account not found: $rawId"),
                )
            call.respond(account.toDto())
        }

        post("/transfers") {
            val dto = call.receive<TransferRequestDto>()
            val request = dto.toDomain().getOrElse { error ->
                return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorDto("invalid_request", error.message ?: "invalid request"),
                )
            }

            when (val result = dependencies.transfers.transfer(request)) {
                is TransferResult.Success -> call.respond(
                    HttpStatusCode.Created,
                    result.transfer.toDto(),
                )
                is TransferResult.Failure -> call.respond(
                    result.error.toHttpStatus(),
                    result.error.toErrorDto(),
                )
            }
        }
    }
}
