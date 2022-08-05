package com.example.trackerapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.trackerapp.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

//        binding.productAddButton.setOnClickListener { addButtonClicked()  }
//        binding.openExcelButton.setOnClickListener { openExcel() }
//
//
//        // Create database
//        val db = Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java, "items-database"
//        )
//            .allowMainThreadQueries()
//            .build()
//
//        val itemDao = db.itemDao()
//        itemDao.nukeTable()
//        itemDao.insertAll(Item(1, "cola", 2.0, 2))
//        var items: List<Item> = itemDao.getAll()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

//    private fun addButtonClicked(){
//
//        Log.i(TAG, "addButtonClicked called")
//
//        val product = binding.productName
//        val price = binding.productPrice
//        val count = binding.productCount.text
//        val productName = product.toString()
//        val productPrice: Double? = price.toString().toDoubleOrNull()
//
//        println("product: $product")
//        println("price: $price")
//        println("count: $count")
//        println("productName: $productName")
//        println("productPrice: $productPrice")
//
//
//        val itemApplication: ItemApplication = ItemApplication();
//        val db = itemApplication.database;
//        db.itemDao().insertAll(Item(1, "cola", 2.0, 2))
//    }
//
//    private fun openExcel(){
//        Log.i(TAG, "openExcel CLICKED")
//    }

}