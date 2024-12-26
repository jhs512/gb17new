package com.ll.backend.global.exceptions

import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty

class ServiceException(resultCode: String, msg: String) : RuntimeException("$resultCode : $msg") {
    private val resultCode by lazy {
        message!!.split(":", limit = 2).toTypedArray()[0].trim()
    }

    private val msg by lazy {
        message!!.split(":", limit = 2).toTypedArray()[1].trim()
    }

    val rsData by lazy {
        RsData<Empty>(resultCode, msg)
    }
}