package com.example.bakery

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform