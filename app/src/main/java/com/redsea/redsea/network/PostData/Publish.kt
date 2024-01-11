package com.redsea.redsea.network.PostData

data class Publish(
    var name: String,
    var from: String,
    var to: String,
    var well_data: MutableList<com.redsea.redsea.network.PostData.WellData>
)