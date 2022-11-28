package kz.example.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import kz.example.shoppinglist.data.ShopListRepositoryImpl
import kz.example.shoppinglist.domain.DeleteShopItemUseCase
import kz.example.shoppinglist.domain.EditShopItemUseCase
import kz.example.shoppinglist.domain.GetShopItemListUseCase
import kz.example.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemListUseCase = GetShopItemListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopItemListUseCase.getShopItemList()

    fun deleteShopItem(shopItem : ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newShopItem)
    }

}