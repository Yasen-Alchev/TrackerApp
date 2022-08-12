package com.example.trackerapp.viewmodels

import android.nfc.Tag
import android.os.Build.VERSION_CODES.P
import android.util.Log
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackerapp.database.item.Product
import com.example.trackerapp.database.item.ProductDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val productDao: ProductDao) : ViewModel() {

    fun getAll(): Flow<List<Product>> = productDao.getAll()

    private fun insertProduct(product: Product){
        println(TAG + " -> insertProduct called")
        productDao.insert(product)
    }

    fun addNewProduct(productName: String, productPrice: Double, productQuantity: Int) {
        CoroutineScope(Dispatchers.IO).launch{
            val productList = productDao.findProductWithNameAndPrice(productName, productPrice)
            val size = productList.size
            var count = productQuantity

            if(size == 0){
                val newProduct = getNewProductEntry(productName, productPrice, productQuantity)
                insertProduct(newProduct)
                return@launch
            }
            count += productList.sumOf { x -> x.productQuantity }
            while(size > 1){
                productDao.delete(productList[size-1])
            }
            productDao.updateProductQuantity(productName, productPrice, count)
        }
    }

    private fun getNewProductEntry(productName: String, productPrice: Double, productQuantity: Int): Product {
        println("getNewProductEntry called")
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

    private fun logThread(methodName: String){
        println("logThread debug: $methodName : ${Thread.currentThread().name}")
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