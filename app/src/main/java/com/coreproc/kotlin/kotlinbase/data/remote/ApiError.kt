package com.coreproc.kotlin.kotlinbase.data.remote

import com.google.gson.Gson
import retrofit2.Response
import timber.log.Timber

object ApiError {

    private const val TAG = "APIERROR"

    fun <T> parseError(response: Response<T>): ErrorBody {
        val errorBody = ErrorBody(500, "Error", "An error occurred", null)

        return try {
            val responseString = response.errorBody()!!.string()
            Timber.e(responseString)

            Gson().fromJson(responseString, ErrorBody::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            errorBody
        }
    }

}
