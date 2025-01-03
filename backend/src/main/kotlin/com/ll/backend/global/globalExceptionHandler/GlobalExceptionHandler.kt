package com.ll.backend.global.globalExceptionHandler

import com.ll.backend.global.app.AppConfig
import com.ll.backend.global.exceptions.ServiceException
import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty
import com.ll.backend.standard.extensions.getOrDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus


@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleException(ex: ServiceException): ResponseEntity<RsData<Empty>> {
        if (AppConfig.isNotProd()) ex.printStackTrace()

        return ResponseEntity
            .status(ex.rsData.statusCode)
            .body(ex.rsData)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(ex: NoSuchElementException): ResponseEntity<RsData<Empty>> {
        if (AppConfig.isNotProd()) ex.printStackTrace()

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                RsData(
                    "404-1",
                    "data not found" + ex.message?.let { " : $it" }.getOrDefault("")
                )
            )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(ex: HttpMessageNotReadableException): ResponseEntity<RsData<Empty>> {
        if (AppConfig.isNotProd()) ex.printStackTrace()

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                RsData(
                    "400-1",
                    "요청 데이터가 올바르지 않습니다."
                )
            )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(ex: MethodArgumentNotValidException): ResponseEntity<RsData<Empty>> {
        if (AppConfig.isNotProd()) ex.printStackTrace()

        val message = ex.bindingResult
            .allErrors
            .asSequence()
            .filterIsInstance<FieldError>()
            .map { error: ObjectError -> error as FieldError }
            .map { error: FieldError -> error.field + "-" + error.code + "-" + error.defaultMessage }
            .sorted()
            .joinToString("\n")

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                RsData(
                    "400-1",
                    message
                )
            )
    }
}