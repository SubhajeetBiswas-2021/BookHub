package com.subhajeet.bookhub.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.subhajeet.bookhub.R
import com.subhajeet.bookhub.adapter.DashboardRecyclerAdapter
import com.subhajeet.bookhub.model.Book
import com.subhajeet.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.Collections


class DashboardFragment : Fragment() {

    lateinit var recyclerDashboard: RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager



    /*val bookList = arrayListOf(
        "P.S.I love You",
        "The Great Gatsby",
        "Anna Karenina",
        "Madame Bovary",
        "War and Peace",
        "Lolita",
        "Middlemarch",
        "The Adventure of Huckleberry Finn",
        "Moby-Dick",
        "The Lord of the Rings"
    )*/

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    val bookInfoList = arrayListOf<Book>()

    var ratingComparator = Comparator<Book>{book1, book2 ->
        if(book1.bookName.compareTo(book2.bookName,true) == 0){
            //sort according to name or alphabetical order
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }

    }




    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recyclerDashboard)



        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE



        layoutManager =LinearLayoutManager(activity)


        val queue = Volley.newRequestQueue(activity as Context)  //variable for storing the queue of request

        val url = "http://13.235.250.119/v1/book/fetch_books/"  //it will give us the response

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {

                    try{
                        progressLayout.visibility = View.GONE
                        val success = it.getBoolean("success")

                        if(success){
                            val data = it.getJSONArray("data")
                            for(i in 0 until data.length()){
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")

                                )
                                bookInfoList.add(bookObject)


                                recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                recyclerDashboard.adapter = recyclerAdapter

                                recyclerDashboard.layoutManager = layoutManager



                            }
                        }else{
                            Toast.makeText(activity as Context,"Some error occurred!!",Toast.LENGTH_SHORT).show()
                        }
                    }catch (e:JSONException) {
                        Toast.makeText(activity as Context,"Some Unexpected error occurred!!",Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {

                    if(activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occured!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }){

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "8c6b73e37020cd"
                    return headers
                }

            }

            queue.add(jsonObjectRequest)

        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open  Settings"){text,listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }

            dialog.setNegativeButton("Exit"){text,listener ->
            ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item?.itemId
        if(id == R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)  //sort the list in incresing order (lowest rating will appear first)
            bookInfoList.reverse()    //this one will make the list of books in such that highest rating book will be up
        }

        recyclerAdapter.notifyDataSetChanged()   //telling the adapter class that the order must be changed which will be displayed
        return super.onOptionsItemSelected(item)
    }


}