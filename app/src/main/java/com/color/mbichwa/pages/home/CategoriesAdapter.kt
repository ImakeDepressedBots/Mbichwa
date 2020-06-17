package com.color.mbichwa.pages.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.color.mbichwa.R
import java.util.ArrayList

class CategoriesAdapter(private var categoryData: ArrayList<Category>, onCategorySelectedListener: OnCategorySelectedListener): RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {


    interface OnCategorySelectedListener{
        fun onCategorySelected(category: Category)
    }

    val listener:OnCategorySelectedListener = onCategorySelectedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_category_item,parent,false))
    }

    override fun getItemCount(): Int {
        return categoryData.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = categoryData[position]
        holder.bind(item,listener)
    }

    class CategoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val categoryNameTextView:TextView = itemView.findViewById(R.id.categoryName)
        val categoryImage:ImageView = itemView.findViewById(R.id.categoryImage)

        fun bind(item: Category, listener: OnCategorySelectedListener){
            categoryNameTextView.text = item.categoryName

            Glide.with(itemView.context)
                .load(item.categoryImageUrl)
                .into(categoryImage)

            itemView.setOnClickListener {
                listener.onCategorySelected(item)
            }
        }

    }
}