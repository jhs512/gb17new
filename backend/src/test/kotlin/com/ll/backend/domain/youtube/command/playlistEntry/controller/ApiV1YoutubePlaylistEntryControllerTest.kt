package com.ll.backend.domain.youtube.command.playlistEntry.controller

import com.ll.backend.domain.youtube.command.playlistEntry.dto.YoutubePlaylistEntryDto
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ApiV1YoutubePlaylistEntryControllerTest @Autowired constructor(
    private val mockMvc: MockMvc
) {
    companion object {
        const val samplePlaylist1Code = "PLE0hRBClSk5I6oSjDBW3HJUAAT-4P6Qo8"

        val samplePlaylist1EntryDtos = listOf(
            YoutubePlaylistEntryDto(
                code = "rsIc0eAqYF0",
                title = "24_12_23, p 13915, 1강, 프로젝트 생성, GIT PUSH",
                position = 0
            ),
            YoutubePlaylistEntryDto(
                code = "oAQp-Pj8h54",
                title = "24 12 23, p 13915, 2강, slog 13902 수업의 63강 소스코드를 복사해서 프로젝트에 적용, POSTMAN 요청도 복사",
                position = 1
            ),
            YoutubePlaylistEntryDto(
                code = "LUgCs8-tHrY",
                title = "24 12 23, p 13915, 3강, 글쓰기에서 고객에게 입력받은 authorId 를 작성자로 지정하도록",
                position = 2
            ),
            YoutubePlaylistEntryDto(
                code = "CYxNh3Rhblc",
                title = "24 12 23, p 13915, 4강, 지금과 같은 방식이라면 3번 회원이 다른 회원인척 글을 작성하는 `사칭`이 가능",
                position = 3
            ),
            YoutubePlaylistEntryDto(
                code = "cJGo6Alb4Xg",
                title = "24 12 23, p 13915, 5강, 신원확인 후 인증까지 해야 안심하고 일을 처리할 수 있습니다.",
                position = 4
            ),
            YoutubePlaylistEntryDto(
                code = "0TEEkuelBSA",
                title = "24 12 23, p 13915, 6강, 이제는 글쓰기시에 작성자 식별을 위해 회원 인증이 필요합니다.(비밀번호)",
                position = 5
            ),
            YoutubePlaylistEntryDto(
                code = "g3IRGrl7A6g",
                title = "24 12 23, p 13915, 7강, 글수정시에는 인증 뿐 아니라 인가까지 필요합니다.",
                position = 6
            ),
            YoutubePlaylistEntryDto(
                code = "kmN9co47ebQ",
                title = "24 12 23, p 13915, 8강, 글삭제시에는 인증 뿐 아니라 인가까지 필요합니다",
                position = 7
            ),
            YoutubePlaylistEntryDto(
                code = "P9GWRZJjCLU",
                title = "24 12 23, p 13915, 9강, 클라이언트는 인증정보를 URL, 헤더, 바디 중 어느곳이던 넣을 수 있습니다",
                position = 8
            ),
            YoutubePlaylistEntryDto(
                code = "7fE0OxGwnh4",
                title = "24 12 23, p 13915, 10강, 보통 인증정보는 헤더에 담는게 맞습니다",
                position = 9
            ),
            YoutubePlaylistEntryDto(
                code = "eW7e1ftUIhQ",
                title = "24 12 23, p 13915, 11강, 모든 액션 메서드에서 인증정보를 헤더에서 가져오도록",
                position = 10
            ),
            YoutubePlaylistEntryDto(
                code = "HaGHOQ0OvLU",
                title = "24 12 23, p 13915, 12강, 인증정보 헤더 2개를 1개로 줄임, 인증을 처리하는 함수를 도입",
                position = 11
            ),
            YoutubePlaylistEntryDto(
                code = "CiHTX2bxSZw",
                title = "24 12 23, p 13915, 13강, POSTMAN 의 요청들을 폴더로 구분, 콜렉션 변수 credentials 를 추가하여 적용, 이제는 사용자 변경을 쉽게",
                position = 12
            ),
            YoutubePlaylistEntryDto(
                code = "EZPYsNKl07Y",
                title = "24 12 23, p 13915, 14강, 관례상 헤더명을 credentials 에서 Authorization 로 변경",
                position = 13
            ),
            YoutubePlaylistEntryDto(
                code = "7R558Aqhdqw",
                title = "24 12 23, p 13915, 15강, POSTMAN 에서 요청에서 Authorization 탭에서 인증용 헤더 쉽게 추가",
                position = 14
            ),
            YoutubePlaylistEntryDto(
                code = "VRtUyOYvqH4",
                title = "24 12 23, p 13915, 16강, POSTMAN 에서 Authorization 탭의 설정은 기본적으로 자식에게 연쇄적으로 상속된다",
                position = 15
            ),
            YoutubePlaylistEntryDto(
                code = "GGZHL4jdmVk",
                title = "24 12 23, p 13915, 17강, Request Scope Bean 방식으로 request 객체를 주입받으면 편합니다",
                position = 16
            ),
            YoutubePlaylistEntryDto(
                code = "DOdeQ-fyRn8",
                title = "24 12 23, p 13915, 18강, 리퀘스트 스코프 빈은 일종의 프록시로 요청자에 따라 실제 객체를 매핑해줍니다.",
                position = 17
            ),
            YoutubePlaylistEntryDto(
                code = "gZk1WlhHDbw",
                title = "24 12 23, p 13915, 19강, 비밀번호 원문이 DB 이외의 장소에 저장되는게 찜찜해서 가입시 password2 값을 랜덤하게 생성",
                position = 18
            ),
            YoutubePlaylistEntryDto(
                code = "xaenjqJfX6o",
                title = "24 12 23, p 13915, 20강, password2를 유니크하게 만들면 그 정보 하나로 신원조회와 인증이 가능합니다",
                position = 19
            ),
            YoutubePlaylistEntryDto(
                code = "Nv_Oz546po8",
                title = "24 12 23, p 13915, 21강, member 테이블에서 id, username, password2 는 식별자, password2는 쉽게 유추할 수 없어서 보안성이 좋다.",
                position = 20
            ),
            YoutubePlaylistEntryDto(
                code = "Ar3eJXz-O2A",
                title = "24 12 23, p 13915, 22강, password2 같은 필드를 관례상 apiKey 라고 한다. password2 대신 apiKey 로 이름변경",
                position = 21
            ),
            YoutubePlaylistEntryDto(
                code = "gLa4DRSlnaU",
                title = "24 12 23, p 13915, 23강, 개발테스트 환경에서만 1번 부터 7번 회원까지는 편의상 apiKey 를 username 과 동일하게 설정",
                position = 22
            ),
            YoutubePlaylistEntryDto(
                code = "kpw9xMjKN9o",
                title = "24 12 23, p 13915, 24강, 로그인 API 구현",
                position = 23
            ),
            YoutubePlaylistEntryDto(
                code = "8W-vfZfxGgo",
                title = "24 12 23, p 13915, 25강, 주민등록증을 분실하는 것 보다 사원증을 분실하는게 덜 위험합니다",
                position = 24
            ),
            YoutubePlaylistEntryDto(
                code = "2x1tzIkP-po",
                title = "24 12 23, p 13915, 26강, 현재 로그인 한 회원의 정보를 조회하는 API 구현",
                position = 25
            ),
            YoutubePlaylistEntryDto(
                code = "apP2-xxSxzs",
                title = "24 12 23, p 13915, 27강, Rq 클래스는 도메인에 종속되지 않는 공통 Request & Response 관련 로직을 모아두는 클래스",
                position = 26
            ),
            YoutubePlaylistEntryDto(
                code = "UI_mALl14ag",
                title = "24 12 24, p 13915, 28강, 댓글 작성 API 설계",
                position = 27
            ),
            YoutubePlaylistEntryDto(
                code = "jAD_DHzH4E8",
                title = "24 12 24, p 13915, 29강, 샘플 데이터(댓글 3개) 생성",
                position = 28
            ),
            YoutubePlaylistEntryDto(
                code = "uG8rDabn880",
                title = "24 12 24, p 13915, 30강, GET http://localhost:8080/api/v1/posts/{postId}/comments",
                position = 29
            ),
            YoutubePlaylistEntryDto(
                code = "EyKm8MC6XS0",
                title = "24 12 24, p 13915, 31강, GET http://localhost:8080/api/v1/posts/{postId}/comments/{id}",
                position = 30
            ),
            YoutubePlaylistEntryDto(
                code = "nrIVfK7zL6o",
                title = "24 12 24, p 13915, 32강, POST http://localhost:8080/api/v1/posts/{postId}/comments",
                position = 31
            ),
            YoutubePlaylistEntryDto(
                code = "JfDrvCZZ9iU",
                title = "24 12 24, p 13915, 33강, 댓글 작성 결과 메세지가 `null번 댓글이 작성되었습니다.` 라고 나오는 이유",
                position = 32
            ),
            YoutubePlaylistEntryDto(
                code = "dSDjwhWiTIo",
                title = "24 12 24, p 13915, 34강, 별도의 함수를 만들고 그 함수에 @Transactional 어노테이션을 붙여서 해결, self 사용 필수",
                position = 33
            ),
            YoutubePlaylistEntryDto(
                code = "avDUfwpEG80",
                title = "24 12 24, p 13915, 35강, 컨트롤러에서 직접 entityManager.flush() 로 return 하기 전에 INSERT 쿼리 강제수행하도록",
                position = 34
            ),
            YoutubePlaylistEntryDto(
                code = "DmOU_bepBoE",
                title = "24 12 24, p 13915, 36강, 컨트롤러에서 서비스의 flush 메서드 호출, 서비스는 리포지터리의 flush 메서드 호출",
                position = 35
            ),
            YoutubePlaylistEntryDto(
                code = "LxB4UvAuWyQ",
                title = "24 12 24, p 13915, 37강, PUT",
                position = 36
            ),
            YoutubePlaylistEntryDto(
                code = "I176S0qaSiU",
                title = "24 12 24, p 13915, 38강, DELETE http://localhost:8080/api/v1/posts/{postId}/comments/{id}",
                position = 37
            ),
            YoutubePlaylistEntryDto(
                code = "hWh6XQGqJcM",
                title = "24 12 24, p 13915, 39강, 관리자는 타인의 글과 댓글을 삭제할 수 있다",
                position = 38
            ),
            YoutubePlaylistEntryDto(
                code = "6D2E0tbz9d0",
                title = "24 12 24, p 13915, 40강, Post 클래스에 checkActorCanDelete, checkActorCanModify",
                position = 39
            ),
            YoutubePlaylistEntryDto(
                code = "9OVHLIsh4z8",
                title = "24 12 24, p 13915, 41강, PostComment 클래스에 checkActorCanDelete, checkActorCanModify",
                position = 40
            )
        )
    }

    @Test
    @DisplayName("GET /api/v1/youtube/command/playlists/$samplePlaylist1Code/entries")
    fun t01() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/youtube/command/playlists/$samplePlaylist1Code/entries")
            )
            .andDo(MockMvcResultHandlers.print())

        // THEN
        resultActions
            .andExpect(status().isOk())

        samplePlaylist1EntryDtos.forEachIndexed { index, dto ->
            resultActions
                .andExpect { jsonPath("\$[$index].code").value(dto.code) }
                .andExpect { jsonPath("\$[$index].title").value(dto.title) }
                .andExpect { jsonPath("\$[$index].position").value(dto.position) }
        }
    }

    @Test
    @DisplayName("GET /api/v1/youtube/command/playlists/NOT-EXISTS/entries")
    fun t02() {
        // WHEN
        val resultActions = mockMvc
            .perform(
                get("/api/v1/youtube/command/playlists/NOT-EXISTS/entries")
            )
            .andDo(MockMvcResultHandlers.print())

        // THEN
        resultActions
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.resultCode").value("404-1"))
    }
}