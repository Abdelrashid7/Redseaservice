package com.redsea.redsea.network.api

import com.redsea.redsea.network.PostData.MakeRequest
import com.redsea.redsea.network.PostData.Publish
import com.redsea.redsea.network.Response.Login.LoginResponse
import com.redsea.redsea.network.Response.MakeRequest.MakeRequestResponse
import com.redsea.redsea.network.Response.OpenRequest.OPenRequests
import com.redsea.redsea.network.Response.PublishWellResponse.PublishWellResponse
import com.redsea.redsea.network.Response.SaveDraft.SaveDraftResponse
import com.redsea.redsea.network.Response.ShowRequest.ShowRequest
import com.redsea.redsea.network.Response.UpdatWellResponse.UpdateWellResponse
import com.redsea.redsea.network.Response.UserWellData.UserWellData
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
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface api {

    @Headers("Accept: application/json; charset=UTF-8")
    @FormUrlEncoded
    @POST("api/auth/access-tokens")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    //----------------option operations--------------

    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/options")
    fun getWells(

        @Header("Authorization") authorization: String

    ): Call<WellOptionsResponse>
    //options survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/surveys")
    fun getWellssurvey(

        @Header("Authorization") authorization: String

    ): Call<WellOptionsResponse>
    //otions test
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/tests")
    fun getWellstest(

        @Header("Authorization") authorization: String

    ): Call<WellOptionsResponse>
    //options trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/troubleshoots")
    fun getWellstrouble(

        @Header("Authorization") authorization: String

    ): Call<WellOptionsResponse>




    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells")
    fun getViewWells(

        @Header("Authorization") authorization: String

    ): Call<ViewWells>
    //----------------make request--------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/requests")
    fun makeReqeust(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : MakeRequest

    ): Call<MakeRequestResponse>
    // make survey request
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-requests")
    fun makeReqeustsurvey(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : MakeRequest

    ): Call<MakeRequestResponse>
    // make request test
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-requests")
    fun makeReqeusttest(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : MakeRequest

    ): Call<MakeRequestResponse>
    // make request trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-requests")
    fun makeReqeusttrouble(

        @Header("Authorization") authorization: String,
        @Body makeWellRequest : MakeRequest

    ): Call<MakeRequestResponse>

//--------------save draft-----------------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData/saveDraft")
    fun saveDraft(

        @Header("Authorization") authorization: String,
        @Body publish: Publish

    ): Call<SaveDraftResponse>


    // trouble save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata/saveDraft")
    fun saveDrafttrouble(
        @Header("Authorization") authorization: String,
        @Body publish: Publish

    ): Call<SaveDraftResponse>
    // survey save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata/saveDraft")
    fun saveDraftsurvey(
        @Header("Authorization") authorization: String,
        @Body publish: Publish

    ): Call<SaveDraftResponse>
    // test save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata/saveDraft")
    fun saveDrafttest(
        @Header("Authorization") authorization: String,
        @Body publish: Publish

    ): Call<SaveDraftResponse>

//----------publish well----------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData")
    fun publishWell(

        @Header("Authorization") authorization: String,
        @Body publish : Publish

    ): Call<PublishWellResponse>

    //publish test
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata")
    fun publishtest(

        @Header("Authorization") authorization: String,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    // publish trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata")
    fun publishtrouble(

        @Header("Authorization") authorization: String,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    // publish survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata")
    fun publishsurvey(

        @Header("Authorization") authorization: String,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    //------------update saved draft--------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData/saveDraft")
    fun upsavedDraft(

        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish: Publish

    ): Call<SaveDraftResponse>


    // trouble save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata/saveDraft")
    fun upsavedDrafttrouble(
        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish: Publish

    ): Call<SaveDraftResponse>
    // survey save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata/saveDraft")
    fun upsavedDraftsurvey(
        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish: Publish

    ): Call<SaveDraftResponse>
    // test save
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata/saveDraft")
    fun upsavedDrafttest(
        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish: Publish

    ): Call<SaveDraftResponse>

    //--------publish saved draft--------
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/wellsData")
    fun publishsavedWell(

        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish : Publish

    ): Call<PublishWellResponse>

    //publish test
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/test-welldata")
    fun publishsavedtest(

        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    // publish trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/troubleshoot-welldata")
    fun publishsavedtrouble(

        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish : Publish

    ): Call<PublishWellResponse>
    // publish survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @POST("api/survey-welldata")
    fun publishsavedsurvey(

        @Header("Authorization") authorization: String,
        @Query("well_id")id: Int,
        @Body publish : Publish

    ): Call<PublishWellResponse>


//------------update well------------------
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/wellsData/{id}")
    fun updatewell(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : Publish
    ):Call<UpdateWellResponse>
    // update survey
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/survey-welldata/{id}")
    fun updatewellsurvey(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : Publish
    ):Call<UpdateWellResponse>
    //update test
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/test-welldata/{id}")
    fun updatewelltest(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : Publish
    ):Call<UpdateWellResponse>
    // update trouble
    @Headers("Accept:application/json ; charset=UTF-8")
    @PATCH("api/troubleshoot-welldata/{id}")
    fun updatewelltrouble(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int,
        @Body publish : Publish
    ):Call<UpdateWellResponse>

//---------userwells------------------
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userWells")
    fun userWells(

        @Header("Authorization") authorization: String,

    ): Call<UserWells>
    // user wells survey
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userSurveyWells")
    fun userWellssurvey(

        @Header("Authorization") authorization: String,

        ): Call<UserWells>
    //user wells test
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userTestWells")
    fun userWellstest(

        @Header("Authorization") authorization: String,

        ): Call<UserWells>
    // user wells trouble
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/wells/userTroubleshootWells")
    fun userWellstrouble(

        @Header("Authorization") authorization: String,

        ): Call<UserWells>



@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/wells/generatePDF/{id}")
fun Wellpdf(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<WellPdfResponse>
// wellpdf survey
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/surveywells/generatePDF/{id}")
fun Wellpdfsurvey(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<WellPdfResponse>
// welllpdf test
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/testwells/generatePDF/{id}")
fun Wellpdftest(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<WellPdfResponse>
//wellpdf trouble
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/troubleshootwells/generatePDF/{id}")
fun Wellpdftrouble(
    @Header("Authorization") authorization: String,
    @Path("id") id:Int
):Call<WellPdfResponse>



@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/requests")
fun openRequest(
    @Header("Authorization") authorization: String
):Call<OPenRequests>
// request survey
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/survey-requests")
fun openRequestsurvey(
    @Header("Authorization") authorization: String
):Call<OPenRequests>

//request test
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/test-requests")
fun openRequesttest(
    @Header("Authorization") authorization: String
):Call<OPenRequests>
//request trouble
@Headers("Accept:application/json ; charset=UTF-8")
@GET("api/troubleshoot-requests")
fun openRequesttrouble(
    @Header("Authorization") authorization: String
):Call<OPenRequests>
//--------fetch request data------------------------------------
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/requests/{id}")
    fun showrequest(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<ShowRequest>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/survey-requests/{id}")
    fun showrequestsurvey(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<ShowRequest>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/test-requests/{id}")
    fun showrequesttest(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<ShowRequest>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/troubleshoot-requests/{id}")
    fun showrequestrouble(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<ShowRequest>

    //fetch draft data
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/wellsData/{id}")
    fun showdraftop(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<UserWellData>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/surveywells/{id}")
    fun showdraftsurv(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<UserWellData>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/testwells/{id}")
    fun showdrafttest(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<UserWellData>
    @Headers("Accept:application/json ; charset=UTF-8")
    @GET("api/troubleshootwells/{id}")
    fun showdrafttrouble(
        @Header("Authorization") authorization: String,
        @Path("id") id:Int
    ):Call<UserWellData>





    //survey wells
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/surveys")
    fun getsurveyWells(

        @Header("Authorization") authorization: String

    ): Call<ViewWells>
    //test wells
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/tests")
    fun gettestWells(

        @Header("Authorization") authorization: String

    ): Call<ViewWells>
    //trouble wells
    @Headers("Accept: application/json ; charset=UTF-8")
    @GET("api/tests")
    fun gettroubleWells(

        @Header("Authorization") authorization: String
    ): Call<ViewWells>



}