package kz.example.shoppinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.example.shoppinglist.R

class ShopItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    val itemName = view.findViewById<TextView>(R.id.text_view_item_name)
    val itemCount = view.findViewById<TextView>(R.id.text_view_item_count)


}