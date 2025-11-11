package com.example.zazulinsepr_1_2.testApiPackage

data class AdviceResponse(
    val slip: Slip
)

data class Slip(
    val id: Int,
    val advice: String
)
