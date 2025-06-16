package com.subhajeet.bookhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao                  //annoting it
interface BookDao {

    @Insert
    fun insertBook(bookEntity: BookEntity)                //use to add a book to a table

    @Delete
    fun deleteBook(bookEntity: BookEntity)


    @Query("SELECT * FROM books")                        // for favourite fragment to display all the entries of the table i.e all that are stored in the database
    fun getAllBooks():List<BookEntity>


    @Query("SELECT * FROM books WHERE book_id = :bookId")    // to check wheather a particular book is added to favourites or not
    fun getBookById(bookId: String): BookEntity
}