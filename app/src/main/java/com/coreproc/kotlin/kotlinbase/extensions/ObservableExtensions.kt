package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.misc.common.SchedulersFacade
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

// IO to MAIN
fun <T> Observable<T>.threadManageIoToUi(): Observable<T> =
    this.subscribeOn(SchedulersFacade.io())
        .observeOn(SchedulersFacade.ui())

fun <T> Flowable<T>.threadManageIoToUi(): Flowable<T> =
    this.subscribeOn(SchedulersFacade.io())
        .observeOn(SchedulersFacade.ui())

fun <T> Single<T>.threadManageIoToUi(): Single<T> =
    this.subscribeOn(SchedulersFacade.io())
        .observeOn(SchedulersFacade.ui())


// COMPUTATION to MAIN
fun <T> Observable<T>.threadManageComputationToUi(): Observable<T> =
    this.subscribeOn(SchedulersFacade.computation())
        .observeOn(SchedulersFacade.ui())

fun <T> Flowable<T>.threadManageComputationToUi(): Flowable<T> =
    this.subscribeOn(SchedulersFacade.computation())
        .observeOn(SchedulersFacade.ui())

fun <T> Single<T>.threadManageComputationToUi(): Single<T> =
    this.subscribeOn(SchedulersFacade.computation())
        .observeOn(SchedulersFacade.ui())


// Observable
fun <T> Observable<T>.addDefaultDoOn(baseViewModel: BaseViewModel): Observable<T> =
    this.doOnComplete { baseViewModel.loading.postValue(false) }
        .doOnSubscribe { baseViewModel.loading.postValue(true) }

fun <T : Any> Observable<T>.defaultSubscribeBy(
    baseViewModel: BaseViewModel,
    onNext: (T) -> Unit
): Disposable =
    subscribe(onNext,
        {
            it.postError(baseViewModel)
        },
        {})
