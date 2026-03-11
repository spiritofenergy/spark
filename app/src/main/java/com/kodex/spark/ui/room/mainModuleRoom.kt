package com.kodex.spark.ui.room

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModuleRoom = module() {
    singleOf(::provideMaimDb)
    viewModelOf(::RoomViewModel)
}