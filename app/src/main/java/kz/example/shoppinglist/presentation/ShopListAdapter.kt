package kz.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.example.shoppinglist.R
import kz.example.shoppinglist.databinding.ShopItemDisabledBinding
import kz.example.shoppinglist.databinding.ShopItemEnabledBinding
import kz.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter: ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when(viewType) {
            SHOP_ITEM_ENABLED -> R.layout.shop_item_enabled
            SHOP_ITEM_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Not found view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout,
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding =holder.binding
        when(binding) {
            is ShopItemDisabledBinding -> {
                binding.textViewItemName.text = shopItem.name
                binding.textViewItemCount.text = shopItem.count.toString()
            }
            is ShopItemEnabledBinding -> {
                binding.textViewItemName.text = shopItem.name
                binding.textViewItemCount.text = shopItem.count.toString()
            }
        }
        binding.root.setOnLongClickListener{
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener{
            onShopItemClickListener?.invoke(shopItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = getItem(position)

        return if(shopItem.enabled) {
            SHOP_ITEM_ENABLED
        }
        else {
            SHOP_ITEM_DISABLED
        }
    }

    companion object {
        const val SHOP_ITEM_ENABLED = 100
        const val SHOP_ITEM_DISABLED = 101

        const val MAX_SIZE_POOL = 15
    }
}