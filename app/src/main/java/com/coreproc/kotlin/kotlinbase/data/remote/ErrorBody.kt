package com.coreproc.kotlin.kotlinbase.data.remote

import com.google.gson.JsonElement
import com.google.gson.JsonObject

class ErrorBody (
    var http_code: Int,
    var code: String,
    override var message: String?,
    var errors: JsonElement?
) : Throwable() {

    fun setCustomMessage(message: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", message)
        this.errors = jsonObject
    }

    fun getFullMessage(): String {
        // parse message here
        var errorDescription = ""

        if (errors == null) {
            return message ?: "An error occured"
        }

        try {
            if (errors!!.isJsonObject) {
                val messageJsonObject = errors as JsonObject?
                val entries = messageJsonObject!!.entrySet()
                for ((key) in entries) {
                    var message = ""
                    if (messageJsonObject.get(key).isJsonPrimitive) {
                        message = messageJsonObject.get(key).asString

                    } else if (messageJsonObject.get(key).isJsonArray) {

                        val jsonArray = messageJsonObject.get(key).asJsonArray
                        if (jsonArray.size() > 0)
                            message += jsonArray.get(0).asString
                    }
                    errorDescription += message + "\n"
                    break
                }

                // We remove next line, if any
                try {
                    errorDescription = errorDescription.substring(0, errorDescription.length - 1)
                } catch (ex: Exception) {
                    // Do nothing
                }

            } else if (errors!!.isJsonArray) {

                val jsonArray = errors!!.asJsonArray
                if (jsonArray.size() > 0)
                    jsonArray.forEach {
                        errorDescription += (it.asString + "\n")
                    }

            } else {
                errorDescription = errors!!.asString
            }

            // Check for message
            this.message?.let {
                errorDescription += it
            }

            return errorDescription
        } catch (ex: Exception) {
            ex.printStackTrace()
            return "An error occurred"
        }

    }

}
