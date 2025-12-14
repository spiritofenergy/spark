package com.kodex.spark.ui.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.firebase.firestore.auth.User
import com.kodex.spark.ui.addScreen.data.Book
import com.kodex.spark.ui.detailScreen.data.RatingData

@Database(
    entities = [RatingData::class],
    version = 1,
    exportSchema = false)
abstract class MainDb: RoomDatabase() {

    abstract val dao: Dao
}
fun provideMaimDb(app: Application): MainDb{
    return Room.databaseBuilder(
        app,
        MainDb::class.java,
        "ratingData.db"
    ).build()
}
