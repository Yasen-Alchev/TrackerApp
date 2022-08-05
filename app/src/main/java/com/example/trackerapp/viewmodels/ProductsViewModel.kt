package com.example.trackerapp.viewmodels

import android.util.Log
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackerapp.database.item.Product
import com.example.trackerapp.database.item.ProductDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val productDao: ProductDao) : ViewModel() {

    fun getAll(): Flow<List<Product>> = productDao.getAll()

    private fun insertProduct(product: Product){
        productDao.insert(product)
    }

    fun addNewProduct(productName: String, productPrice: Double, productQuantity: Int) {


        CoroutineScope(Dispatchers.IO).launch{
            val newProduct = getNewProductEntry(productName, productPrice, productQuantity)
            val productFlowList = productDao.findProductWithNameAndPrice(productName, productPrice)
            var count = productQuantity

            productFlowList.collect {
                println("it = $it")
                println("size = ${it.size}")

                if(it.size == 0){
                    insertProduct(newProduct)
                    return@collect
                }
                else if(it.size > 1){
                    count += it.sumOf { x -> x.productQuantity }
                    while(it.size > 1){
                        it.get(0)
                    }
                    println("count = $count")
                    productDao.updateProductQuantity(productName, productPrice, count)
                    return@collect
                }
                productDao.updateProductQuantity(productName, productPrice, productQuantity)
                return@collect
            }
            productDao.updateProductQuantity(productName, productPrice, productQuantity)
            return@launch
        }
    }

    private fun getNewProductEntry(productName: String, productPrice: Double, productQuantity: Int): Product {
        return Product(
            productName = productName,
            productPrice = productPrice,
            productQuantity = productQuantity,
            date = Date()
        )
    }

    fun isEntryValid(productName: String, productPrice: Double?, productCount: Int?): Boolean {
        if (productName.isBlank() || productPrice == null || productCount == null || productName.toIntOrNull() != null) {
            Log.e(TAG, "Invalid data:\nproductName -> $productName\n, productCount -> $productCount\n, productName -> $productName")
            return false
        }
        return true
    }
}

class ProductsViewModelFactory(private val productDao: ProductDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductsViewModel(productDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}