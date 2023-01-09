package kz.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import kz.example.shoppinglist.R

@BindingAdapter("errorInputName")
fun errorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    val errorNameMessage = if(isError){
        textInputLayout.context.getString(R.string.error_input_name)
    }
    else {
        null
    }
    textInputLayout.error = errorNameMessage
}

@BindingAdapter("errorInputCount")
fun errorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    val errorCountMessage = if(isError){
        textInputLayout.context.getString(R.string.error_input_count)
    }
    else {
        null
    }
    textInputLayout.error = errorCountMessage
}

