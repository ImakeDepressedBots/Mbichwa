package com.color.mbichwa.pages.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.color.mbichwa.R
import com.color.mbichwa.pages.home.models.ProductOption

class ProductOptionAdapter(private var optionsData: ArrayList<ProductOption>, onProductOptionSelectedListener: OnProductOptionSelectedListener): RecyclerView.Adapter<ProductOptionAdapter.ProductOptionViewHolder>() {

    interface OnProductOptionSelectedListener{
        fun onProductOptionSelected(productOption: ProductOption)
    }

    val listener: OnProductOptionSelectedListener = onProductOptionSelectedListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductOptionViewHolder {
        return ProductOptionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_product_option_item,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return optionsData.size
    }

    override fun onBindViewHolder(holder: ProductOptionViewHolder, position: Int) {
        val item = optionsData[position]
        holder.bind(item,listener)
    }

    class ProductOptionViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val productOptionNameTextView:TextView = itemView.findViewById(R.id.productOptionNameTextView)

        fun bind(item:ProductOption,listener: OnProductOptionSelectedListener){
            productOptionNameTextView.text = item.productOptionName
        }

    }
}