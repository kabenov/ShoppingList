package kz.example.shoppinglist.domain

interface ShopListRepository {

    fun getShopItemList(): List<ShopItem>

    fun getShopItem(id: Int): ShopItem

    fun addShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun deleteShopItem(shopItem: ShopItem)
}