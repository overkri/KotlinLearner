package ledger.springbridge

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleNotFound(error: AccountNotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                code = "account_not_found",
                message = error.message ?: "Account not found",
            ),
        )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(error: IllegalArgumentException): ResponseEntity<ErrorResponse> =
        ResponseEntity.badRequest().body(
            ErrorResponse(
                code = "invalid_request",
                message = error.message ?: "Invalid request",
            ),
        )
}
