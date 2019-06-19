package com.coreproc.kotlin.kotlinbase.data.remote

import android.util.Log
import com.google.gson.Gson
import retrofit2.Response

object ApiError {

    private const val TAG = "APIERROR"

    fun <T> parseError(response: Response<T>): ErrorBody {
        val errorBody = ErrorBody(500, "Error", "An error occurred", null)

        return try {
            val responseString = response.errorBody()!!.string()
            Log.e(TAG, responseString)

            Gson().fromJson(responseString, ErrorBody::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            errorBody
        }
    }

}
