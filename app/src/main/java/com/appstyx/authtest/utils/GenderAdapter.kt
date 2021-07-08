package com.appstyx.authtest.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appstyx.authtest.R
import com.appstyx.authtest.databinding.GenderAdapterBinding
import com.appstyx.authtest.models.Gender

class GenderAdapter(private var list: List<Gender.Data.Gender>): BaseAdapter() {

//    inner class GenderViewHolder(inflater: LayoutInflater, parent: ViewGroup):
//            RecyclerView.ViewHolder(inflater.inflate(R.layout.gender_adapter, parent, false)) {
//
//        private val binding = GenderAdapterBinding.inflate(inflater)
//
//        private var gender: RadioButton = binding.gender
//
//                fun bind(genders: Gender.Data.Gender) {
//                    gender.text = genders.name
//                }
//
//            }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenderViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//
//        return GenderViewHolder(inflater, parent)
//    }
//
//    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
//        val genders: Gender.Data.Gender = list[position]
//
//        holder.bind(genders)
//    }
//
//    override fun getItemCount(): Int = list.size

    fun updateList(genderList: List<Gender.Data.Gender>) {

        list = genderList
        notifyDataSetChanged()
    }

    override fun getCount(): Int = list.size

    override fun getItem(position: Int): Any {
       return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        if (convertView == null) {
            val inflater = LayoutInflater.from(parent?.context)

            return inflater.inflate(R.layout.gender_adapter, parent, false)
        }

        val genderText = convertView.findViewById<TextView>(R.id.gender_text)

        genderText.text = list[position].name

        return genderText
    }
}