package com.redsea.redsea.network.Response.Login

import java.io.Serializable

data class LoginResponse(
    val code: Int,
    val token: String,
    val user: User
): Serializable