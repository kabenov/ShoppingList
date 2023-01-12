package kz.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import kz.example.shoppinglist.domain.ShopItem
import kz.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl(application: Application) : ShopListRepository {

//    private val shopItemListLiveData = MutableLiveData<List<ShopItem>>()
//    private val shopItemList = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})
//
//    private var autoIncrementShopItemId = 0
//
//    init {
//        for(i in 0 until 50){
//            val item = ShopItem("Name $i", i, Random.nextBoolean())
//            addShopItem(item)
//        }
//    }

    private val shopListDao = AppDatabase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    override fun getShopItemList(): LiveData<List<ShopItem>> =
    MediatorLiveData<List<ShopItem>>().apply {
        addSource(shopListDao.getShopList()) {
            value = mapper.mapDbModelListToShopList(it)
        }
    }

//    override fun getShopItemList(): LiveData<List<ShopItem>> =
//    Transformations.map(shopListDao.getShopList()) {
//        mapper.mapDbModelListToShopList(it)
//    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val shopItemDbModel = shopListDao.getShopItemById(shopItemId)
        return mapper.mapDbModelToEntity(shopItemDbModel)
    }

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }
}