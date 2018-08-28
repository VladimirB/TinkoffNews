package com.app.tinkoff.news.network

data class Response<T>(val resultCode: String, val payload: T)