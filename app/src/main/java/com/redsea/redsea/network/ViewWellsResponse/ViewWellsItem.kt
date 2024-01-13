package com.redsea.redsea.network.ViewWellsResponse

import java.io.Serializable

data class ViewWellsItem(
    val created_at: String,
    val from: String,
    val id: Int,
    val name: String,
    val published: String,
    val rig: Any,
    val to: String,
    val updated_at: String,
    val user: User,
    val user_id: String,
    val well: Any
) :Serializable