package com.ll.backend.domain.home.home.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.net.InetAddress


@Controller
class HomeController {
    @GetMapping("/")
    @ResponseBody
    fun showMain(): String {
        val localHost = InetAddress.getLocalHost()

        // getHostName 메소드를 호출하여 호스트 이름을 얻습니다.
        val hostname = localHost.hostName

        return "hostname : $hostname"
    }
}