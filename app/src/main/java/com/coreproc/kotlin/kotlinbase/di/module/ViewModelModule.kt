package com.coreproc.kotlin.kotlinbase.di.module

import androidx.lifecycle.ViewModel
import com.coreproc.kotlin.kotlinbase.di.ViewModelKey
import com.coreproc.kotlin.kotlinbase.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel
}