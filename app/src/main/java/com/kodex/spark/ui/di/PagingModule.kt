package com.kodex.spark.ui.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.mainScreen.data.BookFactoryPaging
import com.kodex.spark.ui.utils.FireStoreManagerPaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(ViewModelComponent::class)
object PagingModule {
    @Provides
    @ViewModelScoped
    fun providesPagingFlow(
        firestoreManagerPaging: FireStoreManagerPaging
    ): Flow<PagingData<Book>>{
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                prefetchDistance = 3,
                initialLoadSize = 30,
            ),
            pagingSourceFactory = { BookFactoryPaging(firestoreManagerPaging) }
        ).flow
    }
}