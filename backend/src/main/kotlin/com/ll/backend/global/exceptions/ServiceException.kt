package com.ll.backend.global.exceptions

import com.ll.backend.global.rsData.RsData
import com.ll.backend.standard.base.Empty

class ServiceException(resultCode: String, msg: String) : RuntimeException("$resultCode : $msg") {
    private val resultCode by lazy {
        rsData.resultCode
    }

    private val msg by lazy {
        rsData.msg
    }

    val rsData by lazy {
        val arr = message!!.split(":", limit = 2).toTypedArray()

        RsData<Empty>(
            arr[0].trim(),
            arr[1].trim(),
        )
    }
}