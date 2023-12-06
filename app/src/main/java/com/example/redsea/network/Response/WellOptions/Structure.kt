package com.example.redsea.network.Response.WellOptions

import com.example.redsea.network.Response.WellOptions.StructureDescription

data class Structure(
    val created_at: String,
    val id: Int,
    val name: String,
    val option_id: String,
    val structure_descriptions: List<StructureDescription>,
    val updated_at: String
)