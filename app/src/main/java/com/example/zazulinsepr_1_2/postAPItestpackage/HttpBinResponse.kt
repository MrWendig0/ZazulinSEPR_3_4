package com.example.zazulinsepr_1_2.postAPItestpackage

data class HttpBinResponse(
    val json: UserRequest?, // JSON, который мы отправили
    val origin: String? // IP адрес клиента, сервер его возвращает
)
