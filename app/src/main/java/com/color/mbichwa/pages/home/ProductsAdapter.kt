package com.color.mbichwa.pages.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.color.mbichwa.R
import com.color.mbichwa.pages.home.models.Product

class ProductsAdapter(var productsData:ArrayList<Product>,onProductSelectedListener: OnProductSelectedListener): RecyclerView.Adapter<ProductsAdapter.ItemViewHolder>() {

    interface OnProductSelectedListener{
        fun onProductSelected(product: Product)
    }

    val listener:OnProductSelectedListener = onProductSelectedListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_product_item,parent,false))
    }

    override fun getItemCount(): Int {
        return productsData.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = productsData[position]
        holder.bind(item,listener)
    }

    class ItemViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val productImageView:ImageView = itemView.findViewById(R.id.productImageView)
        val productNameTextView:TextView = itemView.findViewById(R.id.productNameTextView)
        val productStartPriceTextView:TextView = itemView.findViewById(R.id.productStartPriceTextView)
        val productDisplayOptionTextView:TextView = itemView.findViewById(R.id.productDisplayOptionTextView)

        fun bind(item:Product,listener: OnProductSelectedListener){

            Glide.with(itemView.context)
                .load(item.productImageUrl)
                .into(productImageView)

            productNameTextView.text = item.productName
            productStartPriceTextView.text = item.productDisplayPrice
            productDisplayOptionTextView.text = item.productOptions.get(0)

            itemView.setOnClickListener {
                listener.onProductSelected(item)
            }
        }
    }

}