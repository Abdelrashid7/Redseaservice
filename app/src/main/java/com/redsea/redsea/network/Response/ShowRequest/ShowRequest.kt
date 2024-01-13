package com.redsea.redsea.network.Response.ShowRequest

import com.redsea.redsea.network.Response.UserWellData.UserWellData

class ShowRequest(
    val id:Int,
    val user_id:String,
    val well_id:String,
    //this can be found here
    val description:String,
    val status:String,
    val created_at:String,
    val updated_at:String,
    val well:UserWellData


)