package com.subhajeet.bookhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.subhajeet.bookhub.R

class AboutAppAdapter(private val faqList: List<Pair<String, String>>) :
    RecyclerView.Adapter<AboutAppAdapter.FaqViewHolder>() {

    class FaqViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val question: TextView = view.findViewById(R.id.txtQuestion)
        val answer: TextView = view.findViewById(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)
        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val (question, answer) = faqList[position]
        holder.question.text = question
        holder.answer.text = answer
    }

    override fun getItemCount(): Int = faqList.size
}