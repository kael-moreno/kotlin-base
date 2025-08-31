package com.coreproc.kotlin.kotlinbase.data.remote

import com.coreproc.kotlin.kotlinbase.data.remote.model.SampleResponse
import com.coreproc.kotlin.kotlinbase.extensions.handleApiCall
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class DataRepository
@Inject
constructor(
    private val apiInterface: ApiInterface
) : DataSource {

    override suspend fun getSomething(): Flow<ResponseHandler<SampleResponse>> {
        return handleApiCall(
            apiCall = { apiInterface.getSomething() },
            onSuccess = { response ->
                // This is nullable.
                // Custom logic when API call is successful
                Timber.d("Successfully received response with ID: ${response.id}")

                // You can save to local database, cache, perform validations, etc.
                // saveToLocalDatabase(response)
                // validateResponse(response)
                // updateCache(response)
            }
        )
    }

    // Another example - handling user data with logging and caching
    // suspend fun getUser(userId: String): Flow<ResponseHandler<UserResponse>> {
    //     return handleApiCall(
    //         apiCall = { apiInterface.getUser(userId) },
    //         onSuccess = { user ->
    //             Timber.i("User fetched: ${user.name}")
    //             // Cache user data locally
    //             cacheUserData(user)
    //         }
    //     )
    // }
}
