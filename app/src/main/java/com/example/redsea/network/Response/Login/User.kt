package com.example.redsea.network.Response.Login

data class User(
    val created_at: String?,
    val current_team_id: String?,
    val email: String,
    val email_verified_at: String?,
    val id: Int,
    val name: String,
    val profile_photo_path: String?,
    val profile_photo_url: String,
    val two_factor_confirmed_at: String?,
    val type: String,
    val updated_at: String?
)