package com.example.trackerapp.database.item

import android.content.ClipData
import androidx.databinding.adapters.Converters
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ProductDao {
    @Query("SELECT *, product.rowid FROM product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT *, product.rowid FROM product WHERE rowid IN (:productsIds)")
    fun loadAllByIds(productsIds: IntArray): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg products: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: Product)

    @Query("UPDATE product SET product_quantity = :productQuantity WHERE product_name = :productName AND product_price = :productPrice")
    fun updateProductQuantity(productName: String, productPrice: Double, productQuantity: Int)

    @Query("SELECT *, product.rowid FROM product WHERE product_name = :productName AND product_price = :productPrice")
    fun findProductWithNameAndPrice(productName: String, productPrice: Double): List<Product>

    @Query("SELECT *, product.rowid FROM product WHERE product_name = :productName AND product_price = :productPrice AND strftime('%Y-%m-%d', date / 1000, 'unixepoch') = :productDate")
    fun findProductWithNamePriceAndDate(productDate: String, productName: String, productPrice: Double): List<Product>

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM product")
    fun nukeTable();
}