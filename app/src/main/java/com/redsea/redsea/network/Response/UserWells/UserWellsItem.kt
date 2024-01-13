package com.redsea.redsea.network.Response.UserWells

import android.widget.ImageView
import com.redsea.redsea.network.Response.WellOptions.StructureDescription
import java.io.Serializable

data class UserWellsItem(
    val created_at: String,
    var from: String,
    val id: Int,
    var name: String,
    val published: String,
    val rig: Any,
    var to: String,
    val updated_at: String,
    val user_id: String,
    val well: Any,

):Serializable