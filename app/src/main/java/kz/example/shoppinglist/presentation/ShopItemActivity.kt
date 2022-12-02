package kz.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kz.example.shoppinglist.R
import kz.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    //initialize on onBind()
    private lateinit var shopItemViewModel: ShopItemViewModel

    //initialize on onBind()
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextCount: EditText
    private lateinit var saveButton: Button

    //initialise on parseIntent()
    private var extraScreenMode = EXTRA_MODE_UNKNOWN
    private var extraShopItemId = ShopItem.UNDEFINED_ID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        onBind()

        launchRightMode()
        errorForTextInputLayout()
        closeScreen()
    }

    private fun launchRightMode() {
        when(extraScreenMode) {
            EXTRA_MODE_ADD -> launchAddMode()
            EXTRA_MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            shopItemViewModel.addShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(extraShopItemId)
        shopItemViewModel.editShopItem.observe(this) {
            fillEditTextFields(it)
        }
        saveButton.setOnClickListener {
            shopItemViewModel.editShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun errorForTextInputLayout() {
        shopItemViewModel.errorInputName.observe(this){
            val errorNameMessage = if(it){
                getString(R.string.error_input_name)
            }
            else {
                null
            }
            textInputLayoutName.error = errorNameMessage
        }
        shopItemViewModel.errorInputCount.observe(this) {
            val errorCountMessage = if(it){
                getString(R.string.error_input_count)
            }
            else {
                null
            }
            textInputLayoutCount.error = errorCountMessage
        }

        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        editTextCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun fillEditTextFields(shopItem: ShopItem) {
        editTextName.setText(shopItem.name)
        editTextCount.setText(shopItem.count.toString())
    }

    private fun parseIntent() {
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Extra screen mode is absent")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != EXTRA_MODE_ADD && mode != EXTRA_MODE_EDIT){
            throw RuntimeException("Unknown screen mode $mode")
        }
        extraScreenMode = mode

        if(extraScreenMode == EXTRA_MODE_EDIT) {
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Shop item id is absent")
            }
            extraShopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }

    }

    private fun onBind() {
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        textInputLayoutName = findViewById(R.id.text_input_layout_name)
        textInputLayoutCount = findViewById(R.id.text_input_layout_count)
        editTextName = findViewById(R.id.edit_text_name)
        editTextCount = findViewById(R.id.edit_text_count)
        saveButton = findViewById(R.id.save_button)
    }

    private fun closeScreen() {
        shopItemViewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }



    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_MODE_UNKNOWN = ""
        private const val EXTRA_MODE_ADD = "mode_add"
        private const val EXTRA_MODE_EDIT = "mode_edit"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

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