package kz.example.shoppinglist.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kz.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListRecyclerView: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        val floatAddItemButton = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        floatAddItemButton.setOnClickListener {
            val intent = ShopItemActivity.getNewIntentAddMode(this)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        shopListRecyclerView = findViewById(R.id.recycler_view_shop_list)

        with(shopListRecyclerView) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter

            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.SHOP_ITEM_ENABLED,
                ShopListAdapter.MAX_SIZE_POOL
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.SHOP_ITEM_DISABLED,
                ShopListAdapter.MAX_SIZE_POOL
            )
        }

        setupShopItemLongClickListener()
        setupShopItemClickListener()

        setupShopItemSwipeListener()
    }

    private fun setupShopItemLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupShopItemClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("ShopItemClick", it.toString())
            val intent = ShopItemActivity.getNewIntentEditMode(this, it.id)
            startActivity(intent)
        }
    }

    private fun setupShopItemSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(shopListRecyclerView)
    }
}