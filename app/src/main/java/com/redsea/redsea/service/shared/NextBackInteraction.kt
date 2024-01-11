package com.redsea.redsea.service.shared

interface NextBackInteraction {
    fun onCLickNext(position: Int)
    fun onCLickBack(position: Int)

    fun onClickOption(position: Int)
    fun onClickDone(position: Int)
}