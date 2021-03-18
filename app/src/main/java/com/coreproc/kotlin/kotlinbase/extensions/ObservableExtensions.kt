package com.coreproc.kotlin.kotlinbase.extensions

import com.coreproc.kotlin.kotlinbase.misc.common.SchedulersFacade
import com.coreproc.kotlin.kotlinbase.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

// IO to MAIN
object ObservableExtensions {
    fun <T> threadManageIoToUi(observable: Observable<T>): Observable<T> =
        observable.subscribeOn(SchedulersFacade.io())
            .observeOn(SchedulersFacade.ui())


    // COMPUTATION to MAIN
    fun <T> threadManageComputationToUi(observable: Observable<T>): Observable<T> =
        observable.subscribeOn(SchedulersFacade.computation())
            .observeOn(SchedulersFacade.ui())


    // Observable
    fun <T> addDefaultDoOn(observable: Observable<T>, baseViewModel: BaseViewModel):
            Observable<T> =
        observable.doOnComplete { baseViewModel.loading.postValue(false) }
            .doOnSubscribe { baseViewModel.loading.postValue(true) }

    fun <T : Any> defaultSubscribeBy(observable: Observable<T>,
        baseViewModel: BaseViewModel,
        onNext: (T) -> Unit
    ): Disposable =
        observable.subscribe(onNext,
            {
                ResponseExtensions.postError(it, baseViewModel)
            },
            {})
}
