package com.color.mbichwa.pages.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.color.mbichwa.R
import com.color.mbichwa.pages.home.models.OrderedProduct

class CartItemsAdapter(var cartItems:List<OrderedProduct>, onItemDeletedListener: OnItemDeletedListener):RecyclerView.Adapter<CartItemsAdapter.CartItemViewHolder>() {

    interface OnItemDeletedListener{
        fun onItemDeleted(orderedProduct: OrderedProduct)
    }

    val deleteItemListener: OnItemDeletedListener = onItemDeletedListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        return CartItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return  cartItems.size
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val item = cartItems[position]
        holder.bind(item,deleteItemListener)
    }

    class CartItemViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        val cartItemImageView:ImageView = itemView.findViewById(R.id.itemInCartImageView)

        val removeImageButton:ImageButton = itemView.findViewById(R.id.removeItemImageButton)
        val itemInCartQuantityTextView:TextView = itemView.findViewById(R.id.itemInCartQuantityTextView)
        val itemInCartNameTextView:TextView = itemView.findViewById(R.id.itemInCartNameTextView)
//        val itemInCartOptionTextView:TextView = itemView.findViewById(R.id.itemInCartOptionTextView)
        val itemInCartPriceTextView:TextView = itemView.findViewById(R.id.itemInCartPriceTextView)

        fun bind(item:OrderedProduct,deletedListener: OnItemDeletedListener){
            Glide.with(itemView.context).load(item.orderedProductImageUrl)
                .apply(RequestOptions().circleCrop())
                .into(cartItemImageView)

            itemInCartNameTextView.text = item.orderedProductName
//            itemInCartOptionTextView.text = item.orderedProductOption
            itemInCartPriceTextView.text = item.orderedProductPrice.toString()
            itemInCartQuantityTextView.text = item.orderedProductQuantity.toString()

            removeImageButton.setOnClickListener {
                deletedListener.onItemDeleted(item)
            }
        }
    }
}