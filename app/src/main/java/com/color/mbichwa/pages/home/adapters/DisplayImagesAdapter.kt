package com.color.mbichwa.pages.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.color.mbichwa.R

class DisplayImagesAdapter(var displayImagesUrls:ArrayList<String>): RecyclerView.Adapter<DisplayImagesAdapter.DisplayImageViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisplayImageViewHolder {
        return DisplayImageViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_display_image_item,parent,false))
    }

    override fun getItemCount(): Int {
        return displayImagesUrls.size
    }

    override fun onBindViewHolder(holder: DisplayImageViewHolder, position: Int) {
        val item = displayImagesUrls[position]
        holder.bind(item)
    }

    class DisplayImageViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        val displayPicImageView:ImageView = itemView.findViewById(R.id.displayImageImageView)

        fun bind(item:String){

            Glide.with(itemView.context)
                .load(item)
                .into(displayPicImageView)
        }

    }
}