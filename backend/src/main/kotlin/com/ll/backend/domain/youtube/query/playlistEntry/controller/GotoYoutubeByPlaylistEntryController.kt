package com.ll.backend.domain.youtube.query.playlistEntry.controller

import com.ll.backend.domain.youtube.query.playlistEntry.service.YoutubeApiPlaylistEntryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/goto/youtube/{playlistCode}")
class GotoYoutubeByPlaylistEntryController(
    private val fqYoutubeApiPlaylistEntryService: YoutubeApiPlaylistEntryService
) {
    @GetMapping("/{number}")
    @ResponseBody
    fun gotoYoutubeByNumber(
        @PathVariable playlistCode: String,
        @PathVariable number: Int
    ): String {
        return """
            <html>
                <head>
                    <meta http-equiv="refresh" content="3;url=https://www.youtube.com/playlist?list=$playlistCode">
                </head>
                <body>
                    <h1>Redirecting...</h1>
                    <h2><span style="color:red;">${number}번</span>째 영상을 확인해주세요.</h2>
                    <p>Redirecting to <a href="https://www.youtube.com/playlist?list=$playlistCode">https://www.youtube.com/playlist?list=$playlistCode</a></p>
                </body>
            </html>
        """.trimIndent()
    }

//    @GetMapping("/{number}")
//    fun gotoYoutubeByNumber(
//        @PathVariable playlistCode: String,
//        @PathVariable number: Int
//    ): ResponseEntity<String> = handleExceptions {
//        val playlistEntryDtos = fqYoutubeApiPlaylistEntryService
//            .findByPlaylistCode(playlistCode)
//            .getOrThrow()
//
//        val playlistEntryDtosIndex = if (number < 0) {
//            playlistEntryDtos.size + number
//        } else {
//            number - 1
//        }
//
//        if (playlistEntryDtosIndex < 0 || playlistEntryDtosIndex >= playlistEntryDtos.size) {
//            val url = "https://www.youtube.com/playlist?list=$playlistCode"
//
//            // ResponseEntity 생성
//            return ResponseEntity.status(HttpStatus.FOUND) // 302 상태 코드
//                .header(HttpHeaders.LOCATION, url) // 리다이렉션 URL 설정
//                .header(HttpHeaders.CACHE_CONTROL, "max-age=1800") // 30분 동안 캐시
//                .build()
//        }
//
//        val playlistEntryDto = playlistEntryDtos[playlistEntryDtosIndex]
//
//        val url = "https://youtu.be/${playlistEntryDto.code}"
//
//        // ResponseEntity 생성
//        return ResponseEntity.status(HttpStatus.FOUND) // 302 상태 코드
//            .header(HttpHeaders.LOCATION, url) // 리다이렉션 URL 설정
//            .header(HttpHeaders.CACHE_CONTROL, "max-age=1800") // 2분 동안 캐시
//            .build()
//    }
}