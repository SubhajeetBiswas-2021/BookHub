package com.subhajeet.bookhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.subhajeet.bookhub.R
import com.subhajeet.bookhub.database.BookDatabase
import com.subhajeet.bookhub.database.BookEntity
import com.subhajeet.bookhub.util.ConnectionManager
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtBookDesc: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    lateinit var toolbar: Toolbar

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = VISIBLE

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"



        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")

                        if (success) {
                            val bookJSONObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE

                            val bookImageURL = bookJSONObject.getString("image")   //for storing the book image this variable is created


                            Picasso.get().load(bookJSONObject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)

                            txtBookName.text = bookJSONObject.getString("name")
                            txtBookAuthor.text = bookJSONObject.getString("author")
                            txtBookPrice.text = bookJSONObject.getString("price")
                            txtBookRating.text = bookJSONObject.getString("rating")
                            txtBookDesc.text = bookJSONObject.getString("description")

                            val bookEntity = BookEntity(                      //this variable is a object of the book entity class that we created to conduct database operations

                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),           //  we had put the values for all the properties
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageURL                       //implemented in line 111
                            )

                            val checkFav =DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()    //get method will tell us wheather the result of the background process is true or false

                            if(isFav){
                                btnAddToFav.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                btnAddToFav.setBackgroundColor(favColor)
                            }else{
                                btnAddToFav.text = "Add to Favourites"
                                val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }
                            btnAddToFav.setOnClickListener{

                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){  //This block will work when a book is not a favourite

                                    val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()  //This variable will store true when the book will get saved as favourites and false when some error occurs
                                    if(result){
                                        Toast.makeText(this@DescriptionActivity,"Book added to favourites",Toast.LENGTH_SHORT).show()

                                        btnAddToFav.text = "Remove from favourites"
                                        val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                        btnAddToFav.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred!",Toast.LENGTH_SHORT).show()
                                    }
                                }else {
                                    val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result = async.get()

                                    if(result){
                                        Toast.makeText(this@DescriptionActivity,"Book removed from favourites",Toast.LENGTH_SHORT).show()

                                        btnAddToFav.text = "Add to favourites"
                                        val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred!",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }


                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@DescriptionActivity, "Volley Error $it", Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = getString(R.string.Security_Key)   //We can also give the token as  headers["token"] = "Token"  ,For security reason had changed
                        return headers
                    }


                }
            queue.add(jsonRequest)


        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)  //This code close the app
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1-> Check DB if the book is favourite or not
        Mode 2-> Save the book into DB as favourite
        Mode 3-> Remove the favourite book
        */

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }

                2 -> {

                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3 -> {

                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }

            }
            return false
        }

    }
}