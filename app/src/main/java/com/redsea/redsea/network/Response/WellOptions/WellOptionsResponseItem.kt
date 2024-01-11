package com.redsea.redsea.network.Response.WellOptions

data class WellOptionsResponseItem(
    val created_at: String,
    val id: Int,
    val name: String,
    val structures: List<com.redsea.redsea.network.Response.WellOptions.Structure>,
    val updated_at: String?,
    var isExpandable : Boolean = false
)