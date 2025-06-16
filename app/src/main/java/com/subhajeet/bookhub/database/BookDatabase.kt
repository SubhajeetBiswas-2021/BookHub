package com.subhajeet.bookhub.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase: RoomDatabase() {

    abstract fun bookDao() : BookDao              //we need to tell that all the functions that we will perform on the data will be performed by the DAO interface
}