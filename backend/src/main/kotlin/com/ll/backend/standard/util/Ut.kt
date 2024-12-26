package com.ll.backend.standard.util

import com.fasterxml.jackson.core.type.TypeReference
import com.ll.backend.global.app.AppConfig
import java.io.FileOutputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

class Ut {
    class json {
        companion object {
            // 객체를 JSON 문자열로 변환
            fun toString(obj: Any): String {
                return AppConfig.objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(obj)
            }

            // JSON 문자열을 객체로 변환 (타입 안전성 제공)
            inline fun <reified T> toObj(jsonStr: String): T {
                return AppConfig.objectMapper.readValue(jsonStr, object : TypeReference<T>() {})
            }
        }
    }

    class file {
        companion object {
            // HTTP로 파일 다운로드
            fun downloadFileByHttp(url: String, dirPathStr: String): String {
                val dirPath = Paths.get(dirPathStr)

                // 디렉토리 생성 (존재하지 않을 경우)
                if (Files.notExists(dirPath)) {
                    Files.createDirectories(dirPath)
                }

                val uri = URI(url)
                val fileName = uri.path.substringAfterLast("/")
                val filePath = dirPath.resolve(fileName)

                // 파일 다운로드 및 저장
                uri.toURL().openStream().use { inputStream ->
                    FileOutputStream(filePath.toFile()).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                return filePath.toString()
            }

            // 파일 이동
            fun moveFile(originFilePathStr: String, destinationFilePathStr: String) {
                val originFilePath = Paths.get(originFilePathStr)
                val destinationFilePath = Paths.get(destinationFilePathStr)

                require(Files.exists(originFilePath)) { "Source file does not exist: $originFilePathStr" }

                // 상위 디렉토리가 없을 경우 생성
                destinationFilePath.parent?.let { parentPath ->
                    if (Files.notExists(parentPath)) {
                        Files.createDirectories(parentPath)
                    }
                }

                Files.move(originFilePath, destinationFilePath)
            }
        }
    }

    class cmd {
        companion object {
            // 명령어를 비동기적으로 실행
            fun runAsync(cmd: String) {
                Thread { run(cmd) }.start()
            }

            // 명령어 실행 및 출력 처리
            fun run(cmd: String) {
                runCatching {
                    val process = ProcessBuilder("bash", "-c", cmd).start()
                    process.waitFor()

                    println("cmd: $cmd")
                    println("=== output start ===")
                    process.inputStream.bufferedReader().useLines { lines ->
                        lines.forEach { println(it) }
                    }
                    println("=== output end ===")
                }.onFailure { e ->
                    println("failed cmd: $cmd")
                    e.printStackTrace()
                }
            }
        }
    }
}
