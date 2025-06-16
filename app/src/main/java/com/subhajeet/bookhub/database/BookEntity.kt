package com.subhajeet.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")        //we must give name if not given it will take default  name of the class and this is known as annotation helps to tell the compiler that what we are creating
data class BookEntity (
    @PrimaryKey val book_id: Int,
    @ColumnInfo("book_name") val bookName: String,
    @ColumnInfo("book_author") val bookAuthor:String,
    @ColumnInfo("book_price") val bookPrice:String,
    @ColumnInfo("book_rating") val bookRating:String,
    @ColumnInfo("book_desc") val bookDesc: String,
    @ColumnInfo("book_image") val bookImage: String

)