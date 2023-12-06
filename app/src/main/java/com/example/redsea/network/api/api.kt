package com.example.redsea.network.api

import com.example.redsea.network.PostData.MakeRequest
import com.example.redsea.network.PostData.Publish
import com.example.redsea.network.Response.Login.LoginResponse
import com.example.redsea.network.Response.MakeRequest.MakeRequestResponse
import com.example.redsea.network.Response.OpenRequest.OPenRequests
import com.example.redsea.network.Response.PublishWellResponse.PublishWellResponse
import com.example.redsea.network.Response.SaveDraft.SaveDraftResponse
import com.example.redsea.network.Response.UserWells.UserWells
import com.example.redsea.network.Response.WellOptions.WellOptionsResponse
import com.example.redsea.network.Response.Wellpdf.WellPdfResponse
import com.example.redsea.network.ViewWellsResponse.ViewWells
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface api {

    @Headers("Accept: application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("api/auth/access-tokens")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWells(

        @Header("Authorization") authorization: String

    ): Call<WellOptionsResponse>

    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells")
    fun getViewWells(

        @Header("Authorization") authorization: String

    ): Call<ViewWells>
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/requests")
    fun makeReqeust(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : MakeRequest

    ): Call<MakeRequestResponse>
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData/saveDraft")
    fun saveDraft(

        @Header("Authorization") authorization: String,
        @Body publish: Publish

    ): Call<SaveDraftResponse>
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData")
    fun publishWell(

        @Header("Authorization") authorization: String,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userWells")
    fun userWells(

        @Header("Authorization") authorization: String,

    ): Call<UserWells>

@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/wells/generatePDF/{id}")
fun Wellpdf(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<WellPdfResponse>

@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/requests")
fun openRequest(
    @Header("Authorization") authorization: String
):Call<OPenRequests>



}