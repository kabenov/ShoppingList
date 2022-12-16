package kz.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kz.example.shoppinglist.R
import kz.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    //initialise on parseIntent()
    private var extraScreenMode = EXTRA_MODE_UNKNOWN
    private var extraShopItemId = ShopItem.UNDEFINED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()

        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    private fun launchRightMode() {
        val shopItemFragment = when (extraScreenMode) {
            EXTRA_MODE_ADD  -> ShopItemFragment.getNewInstanceAddMode()
            EXTRA_MODE_EDIT -> ShopItemFragment.getNewInstanceEditMode(extraShopItemId)
            else            -> throw RuntimeException("Unknown screen mode $extraScreenMode")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, shopItemFragment)
            .commit()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Extra screen mode is absent")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }

        extraScreenMode = mode
        if (extraScreenMode == EXTRA_MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Shop item id is absent")
            }
            extraShopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "screen_mode"
        private const val EXTRA_MODE_UNKNOWN = ""
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"

        fun getNewIntentAddMode(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_MODE_ADD)
            return intent
        }

        fun getNewIntentEditMode(context: Context, editedShopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, editedShopItemId)
            return intent
        }
    }
}