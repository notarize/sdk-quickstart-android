package com.notarize.androidquickstart.networking

import retrofit2.Call
import retrofit2.http.*

interface NotarizeApi {

    @Headers("Content-Type: application/json")
    @POST("v1/transactions/")
    fun getToken(
        @Header("ApiKey") apiKey: String,
        @Body request: TransactionRequest
    ): Call<SignerResponse>
}