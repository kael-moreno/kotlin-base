package com.coreproc.kotlin.kotlinbase.data.remote

import io.reactivex.Observable

/**
 * Use cases are the entry points to the domain layer.
 *
 * @param <Q> the request type
 * @param <P> the response type
</P></Q> */
abstract class UseCase<Q, P> {

    fun run(requestValues: Q): Observable<P> {
        return executeUseCase(requestValues)
    }

    protected abstract fun executeUseCase(requestValues: Q): Observable<P>
}