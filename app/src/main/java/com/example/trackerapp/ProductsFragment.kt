package com.example.trackerapp

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackerapp.adapters.ProductsAdapter
import com.example.trackerapp.database.ProductsApplication
import com.example.trackerapp.databinding.FragmentProductsBinding
import com.example.trackerapp.viewmodels.ProductsViewModel
import com.example.trackerapp.viewmodels.ProductsViewModelFactory
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext


class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val viewModel: ProductsViewModel by activityViewModels {
        ProductsViewModelFactory(
            (activity?.application as ProductsApplication).database.productDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val productsAdapter = ProductsAdapter {}
        recyclerView.adapter = productsAdapter

        lifecycle.coroutineScope.launch {
            viewModel.getAll().collect {
                productsAdapter.submitList(it)
            }
        }

        binding.productAddButton.setOnClickListener {
            addNewProduct()
        }

        // When Enter is pressed on last edit text box add item by default,
        binding.productQuantity.setOnEditorActionListener{ _, actionId, event ->
            if(KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_UP == event.action){
                addNewProduct()
            }
            true
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun clearInputFields() {
        binding.productName.setText("")
        binding.productPrice.setText("")
        binding.productQuantity.setText("")
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.productName.text.toString(),
            binding.productPrice.text.toString().toDoubleOrNull(),
            binding.productQuantity.text.toString().toIntOrNull()
        )
    }

    private fun addNewProduct() {
        if (isEntryValid()) {
            viewModel.addNewProduct(
                binding.productName.text.toString(),
                binding.productPrice.text.toString().toDouble(),
                binding.productQuantity.text.toString().toInt(),
            )
            activity?.let { it -> hideKeyboard(it) }
        }
        else{
            val toast = Toast.makeText(context, "Invalid Data", Toast.LENGTH_LONG)
            toast.show()
        }
        activity?.let { it -> hideKeyboard(it) }
        //clearInputFields()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
