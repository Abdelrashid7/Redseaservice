package com.redsea.redsea.network.Response.WellOptions

import com.redsea.redsea.network.Response.WellOptions.StructureDescription

data class Structure(
    val created_at: String,
    val id: Int,
    val name: String,
    val option_id: String,
    val structure_descriptions: List<com.redsea.redsea.network.Response.WellOptions.StructureDescription>,
    val updated_at: String
)