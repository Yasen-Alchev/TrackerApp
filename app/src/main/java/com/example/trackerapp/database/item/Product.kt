package com.example.trackerapp.database.item

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

//, primaryKeys = ["itemName", "itemPrice"]

@Fts4
@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true) @NonNull @ColumnInfo(name = "rowid")
    val uid: Int = 0,
    @NonNull @ColumnInfo(name = "product_name")
    val productName: String,
    @NonNull @ColumnInfo(name = "product_price")
    val productPrice: Double,
    @NonNull @ColumnInfo(name = "product_quantity")
    val productQuantity: Int,
    @NonNull @ColumnInfo(name = "date") @TypeConverters(DateTypeConverter::class)
    var date: Date
){
    override fun toString(): String {
        return "$productName -> $productPrice lv -> $productQuantity units"
    }

    fun getForDay(): Date? {
        return date
    }

    fun setForDay(date: Date) {
        this.date = date
    }

}

