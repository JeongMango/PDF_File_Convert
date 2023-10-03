package com.example.filechangepdf.retrofit

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Query
import retrofit2.http.QueryMap

class RetrofitManager {
    companion object {
        val server = RetrofitManager()
    }

    private val retrofit = RetrofitClient.getInstance().create(RetrofitInterface::class.java)




    //------------------------------------------------------------------------------------------
    /*fun requestImg(
        url: String,
        boxHistoryId: RequestBody,
        img: MultipartBody.Part?,
        token: String,
        completion: (String, String, String?) -> Unit,
    ) {
        retrofit.multiPartPara(url, boxHistoryId, img, token).let {
            it.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    val getData = onResumeCommon(response)
                    completion(
                        getData.code,
                        getData.message,
                        getData.objects
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    completion("ERROR", t.toString(), null)
                }

            })
        }
    }


    }*/

    /*fun onResumeCommon(response: Response<JsonElement>): Completion {
        if (response.body() == null) {
            return Completion("", "", "", "")
        } else {
            val jsonBody = JSONObject(response.body().toString())
            val commonData = Gson().fromJson(jsonBody.toString(), CommonData::class.java)
            var data: Any
            if (commonData.objects != null) {
                data = JSONObject(response.body().toString())["data"]
            } else {
                data = ""
            }

            if (response.isSuccessful) {
                return Completion(
                    commonData.code,
                    commonData.message,
                    data.toString(),
                    commonData.localDateTime
                )
            } else {
                return Completion(
                    commonData.code,
                    response.raw().toString(),
                    data.toString(),
                    commonData.localDateTime
                )
            }
        }
    }*/


    data class Completion(
        val code: String,
        var message: String,
        val objects: String? = "null",
        val localDateTime: String
    )

}


enum class RESULT {
    EXPIRED_TOKEN,
    FAIL_REGENERATE_TOKEN
}
