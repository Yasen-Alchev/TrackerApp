package com.example.trackerapp.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trackerapp.database.item.DateTypeConverter
import com.example.trackerapp.database.item.Product
import com.example.trackerapp.database.item.ProductDao

@Database(entities = arrayOf(Product::class), version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class ProductsDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductsDatabase? = null

        fun getDatabase(context: Context): ProductsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    ProductsDatabase::class.java,
                    "products_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}
