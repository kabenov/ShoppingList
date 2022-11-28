package kz.example.shoppinglist.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.example.shoppinglist.R
import kz.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter: RecyclerView.Adapter<ShopItemViewHolder>() {

    var tempCount = 0

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d("ShopListAdapter", "onCreateViewHolder ${++tempCount}")
        val layout = when(viewType) {
            SHOP_ITEM_ENABLED -> R.layout.shop_item_enabled
            SHOP_ITEM_DISABLED -> R.layout.shop_item_disabled
            else -> throw RuntimeException("Not found view type: $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shopItem = shopList[position]
        holder.itemName.text = shopItem.name
        holder.itemCount.text = shopItem.count.toString()
        holder.view.setOnLongClickListener{
            true
        }

    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val shopItem = shopList[position]

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