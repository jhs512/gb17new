package com.ll.backend.standard.extensions

import com.ll.backend.global.exceptions.ServiceException
import org.springframework.web.client.HttpClientErrorException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun <T : Any> T?.getOrDefault(default: T): T {
    return this ?: default
}

fun <T : Any> T?.getOrThrow(): T {
    return this ?: throw NoSuchElementException()
}

fun String.toLocalDateTime(): LocalDateTime {
    return ZonedDateTime.ofInstant(Instant.parse(this), ZoneId.of("Asia/Seoul")).toLocalDateTime()
}

inline fun <T> handleExceptions(block: () -> T): T {
    return try {
        block()
    } catch (e: NoSuchElementException) {
        throw e
    } catch (e: NullPointerException) {
        throw ServiceException("500-1", "API 규격 변경으로인해서 파싱 오류가 발생했습니다.")
    } catch (e: HttpClientErrorException) {
        throw ServiceException("${e.statusCode.value()}-1", e.message ?: "요청 처리 중 오류가 발생했습니다.")
    } catch (e: Exception) {
        throw ServiceException("500-2", "요청 처리 중 오류가 발생했습니다.")
    }
}