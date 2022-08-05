package com.example.trackerapp.database

import android.app.Application

class ProductsApplication: Application() {
    val database: ProductsDatabase by lazy { ProductsDatabase.getDatabase(this) }
}