package com.appstyx.authtest.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appstyx.authtest.R
import com.appstyx.authtest.models.Gender

typealias genderClickListener = (Gender.Data.Gender) -> Unit

class GenderAdapter(private var list: List<Gender.Data.Gender>, private val clickListener: genderClickListener):
    RecyclerView.Adapter<GenderAdapter.GenderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return GenderViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        val episode: Gender.Data.Gender = list[position]

        holder.bind(episode)
    }

    fun updateList(genderList: List<Gender.Data.Gender>) {
        list = genderList
        notifyDataSetChanged()
    }

    inner class GenderViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.gender_adapter, parent, false)), View.OnClickListener {

        private val gender: TextView = itemView.findViewById(R.id.gender_text)

        fun bind(list: Gender.Data.Gender) {

            gender.text = list.id

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val gender = list[adapterPosition]
            clickListener.invoke(gender)
        }
    }
}