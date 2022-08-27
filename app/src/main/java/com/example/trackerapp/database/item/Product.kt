package com.example.trackerapp.database.item

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

//, primaryKeys = ["itemName", "itemPrice"]

//@Fts4
@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = "rowid")
    val uid: Int = 0,
    @NonNull @ColumnInfo(name = "date") @TypeConverters(DateTypeConverter::class)
    var date: Date,
    @NonNull @ColumnInfo(name = "product_name")
    var productName: String,
    @NonNull @ColumnInfo(name = "product_price")
    var productPrice: Double,
    @NonNull @ColumnInfo(name = "product_quantity")
    var productQuantity: Int,

){
    override fun toString(): String {
        return "$productName -> $productPrice lv -> $productQuantity units"
    }
}

