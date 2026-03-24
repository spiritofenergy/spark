package com.kodex.spark.ui.room

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModuleRoom = module() {
    singleOf(::provideMaimDb)
    viewModelOf(::RoomViewModel)
}