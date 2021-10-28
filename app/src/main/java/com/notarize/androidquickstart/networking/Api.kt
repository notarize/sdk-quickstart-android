package com.notarize.androidquickstart.networking

import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.*

class Api {

    @RequiresApi(Build.VERSION_CODES.O)
    fun createTransaction(apiKey: String, signer: SignerRequest, pdfLocation: String): Call<SignerResponse> {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        val api = Retrofit.Builder()
            .baseUrl("https://api-next.notarize.com/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NotarizeApi::class.java)
        return api.getToken(apiKey, generateRequestBody(signer, pdfLocation))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateRequestBody(signer: SignerRequest, pdfLocation: String): TransactionRequest {
        val path = File(pdfLocation)
        val encoded = Base64.getEncoder().encodeToString(path.readBytes()) ?: error("Unable to generate request body")
        return TransactionRequest(listOf(DocumentResource(encoded)), listOf(signer), false)
    }
}