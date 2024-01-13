package com.redsea.redsea.network.Response.UserWellData

import com.redsea.redsea.network.PostData.WellData

class UserWellData(
    val id:Int,
    val name:String,
    val from:String,
    val to :String,
    val well:String,
    val rig:String,
    val user_id:String,
    val published:String,
    val created_at:String,
    val updated_at:String,
    val well_data:ArrayList<WellData>
) {
}