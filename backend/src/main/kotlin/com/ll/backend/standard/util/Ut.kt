package com.ll.backend.standard.util

import com.fasterxml.jackson.core.type.TypeReference
import com.ll.backend.global.app.AppConfig

class Ut {
    class json {
        companion object {
            fun toString(obj: Any): String {
                return AppConfig.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }

            inline fun <reified T> toObj(jsonStr: String): T {
                return AppConfig.objectMapper.readValue(jsonStr, object : TypeReference<T>() {})
            }
        }
    }
}