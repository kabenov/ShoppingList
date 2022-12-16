package kz.example.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import kz.example.shoppinglist.R
import kz.example.shoppinglist.domain.ShopItem
import java.lang.RuntimeException

class ShopItemFragment: Fragment() {

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    //initialize on onBind()
    private lateinit var shopItemViewModel: ShopItemViewModel

    //initialize on onBind()
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutCount: TextInputLayout
    private lateinit var editTextName: EditText
    private lateinit var editTextCount: EditText
    private lateinit var saveButton: Button

    //initialize on
    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind(view)

        launchRightMode()
        errorForTextInputLayout()
        closeScreen()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        }
        else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    private fun launchRightMode() {
        when(this.screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            shopItemViewModel.addShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        shopItemViewModel.getShopItem(this.shopItemId)
        shopItemViewModel.editShopItem.observe(viewLifecycleOwner) {
            fillEditTextFields(it)
        }
        saveButton.setOnClickListener {
            shopItemViewModel.editShopItem(editTextName.text?.toString(), editTextCount.text?.toString())
        }
    }

    private fun fillEditTextFields(shopItem: ShopItem) {
        editTextName.setText(shopItem.name)
        editTextCount.setText(shopItem.count.toString())
    }

    private fun errorForTextInputLayout() {
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner){
            val errorNameMessage = if(it){
                getString(R.string.error_input_name)
            }
            else {
                null
            }
            textInputLayoutName.error = errorNameMessage
        }
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
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

    private fun parseArgs() {
        val args = requireArguments()

        if (!args.containsKey(SCREEN_MODE)) {
            throw java.lang.RuntimeException("Screen mode is absent")
        }

        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException("Unknown screen mode $mode")
        }

        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw java.lang.RuntimeException("Shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun onBind(view: View) {
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        textInputLayoutName = view.findViewById(R.id.text_input_layout_name)
        textInputLayoutCount = view.findViewById(R.id.text_input_layout_count)
        editTextName = view.findViewById(R.id.edit_text_name)
        editTextCount = view.findViewById(R.id.edit_text_count)
        saveButton = view.findViewById(R.id.save_button)
    }

    private fun closeScreen() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
    }


    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }


    companion object {
        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_UNKNOWN = ""
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val SHOP_ITEM_ID = "shop_item_id"

        fun getNewInstanceAddMode(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun getNewInstanceEditMode(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}