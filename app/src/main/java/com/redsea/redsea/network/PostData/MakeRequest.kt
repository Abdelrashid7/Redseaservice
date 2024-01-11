package com.redsea.redsea.network.PostData

import com.google.gson.annotations.SerializedName

data class MakeRequest (
    @SerializedName("well_id")
    val wellId: Int,
    @SerializedName("description")
    val description: String
)