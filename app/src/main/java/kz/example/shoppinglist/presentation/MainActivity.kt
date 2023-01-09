package kz.example.shoppinglist.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kz.example.shoppinglist.R
import kz.example.shoppinglist.databinding.ActivityMainBinding
import kz.example.shoppinglist.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
//    private lateinit var shopListRecyclerView: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

//    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        shopItemContainer = findViewById(R.id.shop_item_container)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

//        val floatAddItemButton = findViewById<FloatingActionButton>(R.id.button_add_shop_item)
        binding.buttonAddShopItem.setOnClickListener {
            if (isPortraitMode()) {
                val intent = ShopItemActivity.getNewIntentAddMode(this)
                startActivity(intent)
            }
            else {
                val shopItemFragment = ShopItemFragment.getNewInstanceAddMode()
                launchFragment(shopItemFragment, "Add")
            }
        }
    }

    private fun setupRecyclerView() {
//        shopListRecyclerView = findViewById(R.id.recycler_view_shop_list)

        with(binding.recyclerViewShopList) {
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
            if (isPortraitMode()) {
                val intent = ShopItemActivity.getNewIntentEditMode(this, it.id)
                startActivity(intent)
            }
            else {
                val shopItemFragment = ShopItemFragment.getNewInstanceEditMode(it.id)
                launchFragment(shopItemFragment, "Edit")
            }
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
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewShopList)
    }

    private fun isPortraitMode(): Boolean {
        return binding.shopItemContainer == null
    }

    private fun launchFragment(shopItemFragment: ShopItemFragment, name: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, shopItemFragment)
            .addToBackStack(name)
            .commit()
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}