package com.ll.backend.standard.extensions

fun <T : Any> T?.getOrDefault(default: T): T {
    return this ?: default
}

fun <T : Any> T?.getOrThrow(): T {
    return this ?: throw NoSuchElementException()
}