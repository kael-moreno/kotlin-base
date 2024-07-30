package com.coreproc.kotlin.kotlinbase.data.remote

data class Resource<out T>(val status: RemoteApiStatusEnum, val data: T? = null, val message: String? = null) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = RemoteApiStatusEnum.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = RemoteApiStatusEnum.ERROR, data = data, message = message)
    }
}