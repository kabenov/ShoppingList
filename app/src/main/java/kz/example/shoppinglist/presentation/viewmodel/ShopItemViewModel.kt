package kz.example.shoppinglist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.example.shoppinglist.data.ShopListRepositoryImpl
import kz.example.shoppinglist.domain.*
import java.lang.Exception

class ShopItemViewModel: ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _editShopItem = MutableLiveData<ShopItem>()
    val editShopItem: LiveData<ShopItem>
        get() = _editShopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen


    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItem(shopItemId)
        _editShopItem.value = shopItem
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val parsedInputName = parseName(inputName)
        val parsedInputCount = parseCount(inputCount)
        val fieldsValid = validateInput(parsedInputName, parsedInputCount)

        if(fieldsValid) {
            val createdShopItem = ShopItem(parsedInputName, parsedInputCount, true)
            addShopItemUseCase.addShopItem(createdShopItem)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val parsedInputName = parseName(inputName)
        val parsedInputCount = parseCount(inputCount)
        val fieldsValid = validateInput(parsedInputName, parsedInputCount)

        if(fieldsValid) {
            _editShopItem.value?.let{
                val editedShopItem = it.copy(name = parsedInputName, count = parsedInputCount)
                editShopItemUseCase.editShopItem(editedShopItem)
                finishWork()
            }
        }
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return try {
            count?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true

        if(name.isBlank()){
            _errorInputName.value = true
            result = false
        }
        if(count <= 0){
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }
}