package com.subhajeet.bookhub.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.subhajeet.bookhub.R
import com.subhajeet.bookhub.adapter.AboutAppAdapter


class AboutAppFragment : Fragment() {

    lateinit var recyclerFaq: RecyclerView
    private lateinit var recyclerAdapter: AboutAppAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_app, container, false)

        // Sample FAQ list
        val faqList = listOf(
            Pair(
                "Q.1 How will the training be delivered?",
                "A.1 You will be taught using pre-recorded videos and text tutorials. The training has quizzes, assignments, and tests to help you learn better. At the end of the training, you will attempt a project to get hands-on practice of what you learn during your training."
            ),
            Pair(
                "Q.2 What will be the timings of the training?",
                "A.2 As this is a purely online training program, you can choose to learn at any time of the day. We will recommend a pace to be followed throughout the program, but the actual timings and learning hours can be decided by students according to their convenience."
            ),
            Pair(
                "Q.3 How much time should I spend everyday?",
                "A.3 We recommend spending 10-12 hours per week. However, the actual learning hours can be decided by you as por your convenience."
            ),
            Pair("Q.4 What is this app for?", "A.4 This app helps you manage tasks efficiently."),
            Pair("Q.5 How do I register?", "A.5 Go to the registration page and fill out the form."),
            Pair("Q.6 Can I reset my password?", "A.6 Yes, use the 'Forgot Password' option."),
            Pair("Q.7 How do I contact support?", "A.7 You can contact us at support@example.com."),
            Pair("Q.8 Is this app free?", "A.8 Yes, this app is completely free to use.")
        )

        recyclerFaq = view.findViewById(R.id.recyclerFaq)

        // Initialize the adapter with the FAQ list
        recyclerAdapter = AboutAppAdapter(faqList)

        // Set up the RecyclerView with a LinearLayoutManager
        recyclerFaq.layoutManager = LinearLayoutManager(context)
        recyclerFaq.adapter = recyclerAdapter

        return view
    }

}