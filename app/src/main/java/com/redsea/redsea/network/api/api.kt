package com.redsea.redsea.network.api

import com.redsea.redsea.network.PostData.MakeRequest
import com.redsea.redsea.network.PostData.Publish
import com.redsea.redsea.network.Response.Login.LoginResponse
import com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse
import com.redsea.redsea.network.Response.OpenRequest.OPenRequests
import com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse
import com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse
import com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse
import com.redsea.redsea.network.Response.UserWells.UserWells
import com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse
import com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse
import com.redsea.redsea.network.ViewWellsResponse.ViewWells
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface api {

    @Headers("Accept: application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("api/auth/access-tokens")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<com.redsea.redsea.network.Response.Login.LoginResponse>


    //----------------option operations--------------

    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWells(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
    //options survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWellssurvey(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
    //otions test
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWellstest(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>
    //options trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWellstrouble(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.Response.WellOptions.WellOptionsResponse>




    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells")
    fun getViewWells(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.ViewWellsResponse.ViewWells>
    //----------------make request--------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/requests")
    fun makeReqeust(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : com.redsea.redsea.network.PostData.MakeRequest

    ): Call<com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse>
    // make survey request
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-requests")
    fun makeReqeustsurvey(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : com.redsea.redsea.network.PostData.MakeRequest

    ): Call<com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse>
    // make request test
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-requests")
    fun makeReqeusttest(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : com.redsea.redsea.network.PostData.MakeRequest

    ): Call<com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse>
    // make request trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-requests")
    fun makeReqeusttrouble(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : com.redsea.redsea.network.PostData.MakeRequest

    ): Call<com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse>

//--------------save draft-----------------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData/saveDraft")
    fun saveDraft(

        @Header("Authorization") authorization: String,
        @Body publish: com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>


    // trouble save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata/saveDraft")
    fun saveDrafttrouble(
        @Header("Authorization") authorization: String,
        @Body publish: com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
    // survey save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata/saveDraft")
    fun saveDraftsurvey(
        @Header("Authorization") authorization: String,
        @Body publish: com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>
    // test save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata/saveDraft")
    fun saveDrafttest(
        @Header("Authorization") authorization: String,
        @Body publish: com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse>

//----------publish well----------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData")
    fun publishWell(

        @Header("Authorization") authorization: String,
        @Body publish : com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse>

    //publish test
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata")
    fun publishtest(

        @Header("Authorization") authorization: String,
        @Body publish : com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse>
    // publish trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata")
    fun publishtrouble(

        @Header("Authorization") authorization: String,
        @Body publish : com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse>
    // publish survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata")
    fun publishsurvey(

        @Header("Authorization") authorization: String,
        @Body publish : com.redsea.redsea.network.PostData.Publish

    ): Call<com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse>

//------------update well------------------
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/wellsData/{id}")
    fun updatewell(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : com.redsea.redsea.network.PostData.Publish
    ):Call<com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse>
    // update survey
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/survey-welldata/{id}")
    fun updatewellsurvey(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : com.redsea.redsea.network.PostData.Publish
    ):Call<com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse>
    //update test
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/test-welldata/{id}")
    fun updatewelltest(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : com.redsea.redsea.network.PostData.Publish
    ):Call<com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse>
    // update trouble
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/troubleshoot-welldata/{id}")
    fun updatewelltrouble(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : com.redsea.redsea.network.PostData.Publish
    ):Call<com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse>

//---------userwells------------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userWells")
    fun userWells(

        @Header("Authorization") authorization: String,

    ): Call<com.redsea.redsea.network.Response.UserWells.UserWells>
    // user wells survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userSurveyWells")
    fun userWellssurvey(

        @Header("Authorization") authorization: String,

        ): Call<com.redsea.redsea.network.Response.UserWells.UserWells>
    //user wells test
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userTestWells")
    fun userWellstest(

        @Header("Authorization") authorization: String,

        ): Call<com.redsea.redsea.network.Response.UserWells.UserWells>
    // user wells trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userTroubleshootWells")
    fun userWellstrouble(

        @Header("Authorization") authorization: String,

        ): Call<com.redsea.redsea.network.Response.UserWells.UserWells>



@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/wells/generatePDF/{id}")
fun Wellpdf(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse>
// wellpdf survey
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/surveywells/generatePDF/{id}")
fun Wellpdfsurvey(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse>
// welllpdf test
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/testwells/generatePDF/{id}")
fun Wellpdftest(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse>
//wellpdf trouble
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/troubleshootwells/generatePDF/{id}")
fun Wellpdftrouble(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<com.redsea.redsea.network.Response.Wellpdf.WellPdfResponse>



@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/requests")
fun openRequest(
    @Header("Authorization") authorization: String
):Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
// request survey
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/survey-requests")
fun openRequestsurvey(
    @Header("Authorization") authorization: String
):Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>

//request test
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/test-requests")
fun openRequesttest(
    @Header("Authorization") authorization: String
):Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>
//request trouble
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/troubleshoot-requests")
fun openRequesttrouble(
    @Header("Authorization") authorization: String
):Call<com.redsea.redsea.network.Response.OpenRequest.OPenRequests>



    //survey wells
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/surveys")
    fun getsurveyWells(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.ViewWellsResponse.ViewWells>
    //test wlls
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/tests")
    fun gettestWells(

        @Header("Authorization") authorization: String

    ): Call<com.redsea.redsea.network.ViewWellsResponse.ViewWells>
    //trouble wells
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/tests")
    fun gettroubleWells(

        @Header("Authorization") authorization: String
    ): Call<com.redsea.redsea.network.ViewWellsResponse.ViewWells>



}