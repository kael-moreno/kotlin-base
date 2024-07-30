package com.coreproc.kotlin.kotlinbase.data.remote

/**
 * Use cases are the entry points to the domain layer.
 *
 * @param <Q> the request type
 * @param <P> the response type
</P></Q> */
abstract class UseCase<Q, P> {

    suspend fun run(requestValues: Q): P {
        return executeUseCase(requestValues)
    }

    protected abstract suspend fun executeUseCase(requestValues: Q): P
}