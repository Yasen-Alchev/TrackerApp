package com.example.trackerapp.database.item

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT *, product.rowid FROM product")
    fun getAll(): Flow<List<Product>>

    @Query("SELECT *, product.rowid FROM product WHERE rowid IN (:productsIds)")
    fun loadAllByIds(productsIds: IntArray): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg products: Product)

    @Update
    fun update(product: Product)

    @Query("UPDATE product SET product_name = :productName, product_price = :productPrice, product_quantity = :productQuantity WHERE product.rowid == :productUid")
    fun update2(productUid: Int, productName: String, productPrice: Double, productQuantity: Int)

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