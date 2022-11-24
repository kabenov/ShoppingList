package kz.example.shoppinglist.data

import kz.example.shoppinglist.domain.ShopItem
import kz.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopItemList = mutableListOf<ShopItem>()
    private var autoIncrementShopItemId = 0

    override fun getShopItemList(): List<ShopItem> {
        return shopItemList.toMutableList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopItemList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found (")
    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementShopItemId++
        }

        shopItemList.add(shopItem)
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        shopItemList.remove(shopItem)
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        deleteShopItem(oldShopItem)
        addShopItem(shopItem)
    }
}