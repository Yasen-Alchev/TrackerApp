package com.example.trackerapp.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackerapp.database.item.DateTypeConverter
import com.example.trackerapp.database.item.Product
import com.example.trackerapp.database.item.ProductDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val productDao: ProductDao) : ViewModel() {

    fun getAll(): Flow<List<Product>> = productDao.getAll()

    private fun insertProduct(product: Product){
        println(TAG + " -> insertProduct called")
        productDao.insert(product)
    }

    fun addNewProduct(productName: String, productPrice: Double, productQuantity: Int) {
        CoroutineScope(Dispatchers.IO).launch{

            val newProduct = getNewProductEntry(productName, productPrice, productQuantity)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val newProductDate = dateFormat.format(newProduct.date)

            val productList = productDao.findProductWithNamePriceAndDate(newProductDate ,productName, productPrice)
            var size = productList.size

            println("size = $size")

            if(size == 0){
                insertProduct(newProduct)
                return@launch
            }

            val mostRecentProduct = productList.maxBy{ x -> x.date}

            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal1.time = mostRecentProduct.date
            cal2.time = newProduct.date

            var sameDay = cal1[Calendar.DAY_OF_YEAR] === cal2[Calendar.DAY_OF_YEAR] &&
                    cal1[Calendar.YEAR] === cal2[Calendar.YEAR]

            if(!sameDay){
                insertProduct(newProduct)
                return@launch
            }

            while(size > 1){
                productDao.delete(productList[size-1])
                size--
            }

            var quantity = productQuantity
            quantity += productList.sumOf { x -> x.productQuantity }
            productDao.updateProductQuantity(productName, productPrice, quantity)
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