package com.example.trackerapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trackerapp.database.item.Product
import com.example.trackerapp.databinding.ProductEntityBinding
import java.text.SimpleDateFormat
import java.util.*

class ProductsAdapter (private val onItemClicked: (Product) -> Unit)
    : ListAdapter<Product, ProductsAdapter.ProductsViewHolder>(DiffCallback){

    class ProductsViewHolder(
        private var binding: ProductEntityBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productDateTextView.text = android.text.format.DateFormat.format("yyyy-MM-dd", product.date).toString()
            binding.productNameTextView.text = product.productName
            binding.productPriceTextView.text = product.productPrice.toString()
            binding.productQuantityTextView.text = product.productQuantity.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val viewHolder = ProductsViewHolder(
            ProductEntityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldProduct: Product, newProduct: Product): Boolean {
                return oldProduct.uid == newProduct.uid
            }

            override fun areContentsTheSame(oldProduct: Product, newProduct: Product): Boolean {
                return oldProduct == newProduct
            }
        }
    }
}