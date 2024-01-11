package com.redsea.redsea.network.Response.OpenRequest

import com.redsea.redsea.network.ViewWellsResponse.ViewWellsItem
import java.io.Serializable


class OpenRequestItem(
    val id:Int,
    val user_id:String,
    val well_id:String,
    val description:String,
    val status:String,
    val created_at:String,
    val updated_at:String,
    val well: com.redsea.redsea.network.ViewWellsResponse.ViewWellsItem

):Serializable