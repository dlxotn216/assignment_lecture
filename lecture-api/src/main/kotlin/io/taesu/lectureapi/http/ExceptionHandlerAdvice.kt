package io.taesu.lectureapi.http

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.common.i18n.MessageService
import io.taesu.lectureapi.common.i18n.SupportLang
import io.taesu.lectureapi.common.utils.ExceptionUtils
import io.taesu.lectureapi.response.ErrorInfo
import io.taesu.lectureapi.response.FailResponse
import io.taesu.lectureapi.response.FieldError
import jakarta.servlet.http.HttpServletRequest
import org.apache.logging.log4j.message.SimpleMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@RestControllerAdvice
class ExceptionHandlerAdvice(
    private val messageService: MessageService,
    private val localeResolver: LocaleResolver,
): ResponseEntityExceptionHandler() {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        exception: BusinessException,
        webRequest: WebRequest,
    ): ResponseEntity<FailResponse> {
        val failResponse = FailResponse(
            ErrorInfo(exception.errorCode),
            messageService.getMessage(exception, resolveSupportLang(webRequest))

        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(failResponse)
    }

    override fun handleHttpMessageNotReadable(
        exception: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val cause = exception.cause
        if (cause is MismatchedInputException) {
            val fieldErrors = cause.path.map {
                FieldError(it.fieldName, resolveFieldErrorMessage(request, it.description))
            }
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(FailResponse(ErrorInfo(ErrorCode.INVALID_REQUEST, fieldErrors), "유효하지 않은 요청입니다."))
        }
        return invalidRequestResponseEntity()
    }

    override fun handleHandlerMethodValidationException(
        exception: HandlerMethodValidationException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        return invalidRequestResponseEntity()
    }

    private fun invalidRequestResponseEntity(): ResponseEntity<Any> {
        val failResponse = FailResponse(ErrorInfo(ErrorCode.INVALID_REQUEST), "유효하지 않은 요청입니다.")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(failResponse)
    }

    override fun handleMethodArgumentNotValid(
        exception: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any> {
        val fieldErrors = this.resolveFieldErrors(exception, request)
        val failResponse = FailResponse(ErrorInfo(ErrorCode.INVALID_REQUEST, fieldErrors), "유효하지 않은 요청입니다.")
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(failResponse)
    }

    fun resolveFieldErrors(
        exception: MethodArgumentNotValidException,
        webRequest: WebRequest,
    ): List<FieldError> {
        return exception.fieldErrors
            .map {
                FieldError(
                    it.field,
                    resolveFieldErrorMessage(webRequest, it.defaultMessage)
                )
            }
    }

    private fun resolveFieldErrorMessage(
        webRequest: WebRequest,
        messageId: String?,
    ): String {
        val supportLang = resolveSupportLang(webRequest)
        return messageId?.let { messageService.getMessage(it, supportLang) }
            ?.takeIf { it.isNotEmpty() }
            ?: messageService.getMessage("MESSAGE.INVALID.FIELD", supportLang)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(
        throwable: Exception,
        webRequest: WebRequest,
    ): ResponseEntity<FailResponse> {
        val rootCause = ExceptionUtils.resolveRootCause(throwable)
        if (rootCause is BusinessException) {
            return handleBusinessException(rootCause, webRequest)
        }

        return handleUnexpectedException(throwable, webRequest)
    }

    private fun handleUnexpectedException(
        throwable: Throwable,
        webRequest: WebRequest,
    ): ResponseEntity<FailResponse> {
        val failResponse = FailResponse(
            ErrorInfo(ErrorCode.UNEXPECTED),
            messageService.getMessage(ErrorCode.UNEXPECTED, resolveSupportLang(webRequest))
        )
        log.error("예기치 못한 예외가 발생했습니다. [{}]", throwable.message, throwable)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(failResponse)
    }

    private fun resolveSupportLang(webRequest: WebRequest): SupportLang {
        val servletWebRequest = webRequest as? ServletWebRequest ?: return SupportLang.DEFAULT
        val httpServletRequest = servletWebRequest.nativeRequest as? HttpServletRequest ?: return SupportLang.DEFAULT
        val locale = localeResolver.resolveLocale(httpServletRequest)
        return SupportLang.from(locale)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
