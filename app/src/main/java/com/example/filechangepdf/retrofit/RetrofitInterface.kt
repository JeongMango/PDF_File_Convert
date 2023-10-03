package com.example.filechangepdf.retrofit

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {
    @Multipart
    @POST
    fun multiPartPara(
        @Url url: String,
        @Part("id") data: RequestBody,
        @Part pdfFIle: MultipartBody.Part?,
//        @Header("Authorization") accessToken: String
    ): Call<JsonElement>


}