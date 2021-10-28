package com.notarize.androidquickstart

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notarize.androidquickstart.networking.Api
import com.notarize.androidquickstart.networking.PhoneNumberInfo
import com.notarize.androidquickstart.networking.SignerRequest
import com.notarize.androidquickstart.networking.SignerResponse
import com.notarize.notarizeandroidquickstart.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        val tokenText = findViewById<TextView>(R.id.token_text)
        val api = Api()
        val assMan = assets


        val onClickGenerate = object: View.OnClickListener {
            override fun onClick(v: View?) {
                var `in`: InputStream? = null
                var out: OutputStream? = null
                val file = File(filesDir, "sample.pdf")
                try {
                    `in` = assMan.open("sample.pdf")
                    out = openFileOutput(file.getName(), Context.MODE_PRIVATE)
                    copyFile(`in`, out)
                    `in`.close()
                    `in` = null
                    out.flush()
                    out.close()
                    out = null
                } catch (e: Exception) {
                    error(e)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val createTransaction = api
                        .createTransaction("<API_KEY>",
                            SignerRequest("<SIGNER_FIRST_NAME>", "<SIGNER_LAST_NAME>", "<SIGNER_EMAIL>",
                            PhoneNumberInfo("1", "<SIGNER_PHONE_NUMBER>")),
                            file.absolutePath
                            )
                    createTransaction.enqueue(object: Callback<SignerResponse> {
                        override fun onResponse(
                            call: Call<SignerResponse>,
                            response: Response<SignerResponse>
                        ) {
                            tokenText.text = response.body()?.signer_info?.sdk_token
                        }

                        override fun onFailure(call: Call<SignerResponse>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Failure!", Toast.LENGTH_LONG).show()
                        }

                    })
                }
            }
        }

        button.setOnClickListener(onClickGenerate)
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }
}