package com.example.myapplication

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel: ViewModel() {
    val ldShop = Shop.getInstance()
    val ldUser = User(300F, 500F, 400F)

    val ldVisability = MutableLiveData(true)

    val ldBalance = MutableLiveData("Баланс (руб):\nКарта: ${ldUser.cardMoney}\n" +
            "Нал: ${ldUser.nalMoney}\nБонусы: ${ldUser.bonusMoney}\n" +
            "Всего: ${ldUser.cardMoney + ldUser.nalMoney + ldUser.bonusMoney}")
    val ldCategoryName = MutableLiveData<String>()
    val ldCategory = MutableLiveData<ArrayAdapter<String>>()
    val ldProduct = MutableLiveData<String>()

    val ldProductInBascket = MutableLiveData<String>()
    val ldBascket = MutableLiveData<ArrayAdapter<String>>()
    val ldPrice = MutableLiveData<Float>()

    init {
        ldCategoryName.value = "Категории"
        ldPrice.value = ldUser.priceInBascket()
    }

    fun switch() {
        if (ldVisability.value == true) {
            ldVisability.value = false
        } else ldVisability.value = true
    }

    fun buy(
        context: Context,
        edCard: EditText,
        edNal: EditText,
        edBonus: EditText,
    ) {
        val price = ldPrice.value?.toString()?.toFloatOrNull() ?: 0f
        val card = edCard.text.toString().toFloatOrNull() ?: 0f
        val nal = edNal.text.toString().toFloatOrNull() ?: 0f
        val bonus = edBonus.text.toString().toFloatOrNull() ?: 0f

        val result = ldUser.buy(price, card, nal, bonus)

        if (result) {
            Toast.makeText(context, "Оплата прошла успешно",
                Toast.LENGTH_SHORT).show()
            clear(context)
        } else {
            Toast.makeText(context, "Оплата не прошла",
                Toast.LENGTH_SHORT).show()
        }
        ldBalance.value = "Баланс (руб):\nКарта: ${ldUser.cardMoney}\n" +
                "Нал: ${ldUser.nalMoney}\nБонусы: ${ldUser.bonusMoney}\n" +
                "Всего: ${ldUser.cardMoney + ldUser.nalMoney + ldUser.bonusMoney}"
    }

    fun add(
        context: Context,
        lv: ListView,
        ed: EditText,
        tv: TextView
    ){
        val num = ed.text.toString()
        val pr = tv.text.toString()
        if (num.isNotEmpty() && pr != "Продукт") {
            if (ldCategoryName.value == "Штучные товары" && num.toIntOrNull() != null) {
                val product = ldShop.toProduct(pr, num.toInt())
                ldUser.addToBasket(product, num.toInt())
            } else if (ldCategoryName.value == "Весовые товары") {
                val product = ldShop.toProduct(pr, num.toFloat())
                ldUser.addToBasket(product, 0, num.toFloat())
            }

            val userProduct = ArrayAdapter(context, R.layout.simple_list_item_1, ldUser.basketString)
            ldBascket.value = userProduct
            lv.adapter = ldBascket.value
            ldPrice.value = ldUser.priceInBascket()
        }
    }

    fun remove(
        context: Context,
        lv: ListView,
        ed: EditText,
        tv: TextView
    ) {
        val tvText = tv.text.toString()
        val num = ed.text.toString()
        val product = ldShop.parseProductFromString(tvText)
        val words = tvText.trim().split(" ").lastOrNull()
        if (num.isNotEmpty() && product != null && tvText != "Продукт") {
            if (num.toIntOrNull() != null && words == "штук") {
                ldUser.removeFromBasket(product, num.toInt())
            } else if (num.toFloatOrNull() != null && words == "кг") {
                ldUser.removeFromBasket(product, 0, num.toFloat())
            }
            val userProduct = ArrayAdapter(context, R.layout.simple_list_item_1, ldUser.basketString)
            ldBascket.value = userProduct
            lv.adapter = ldBascket.value
            ldPrice.value = ldUser.priceInBascket()
        }
    }

    fun clear(context: Context) {
        ldUser.clearBasket()
        ldBascket.value = ArrayAdapter<String>(context, R.layout.simple_list_item_1, mutableListOf())
        ldPrice.value = ldUser.priceInBascket()
    }

    fun swap(
        context: Context,
        lv: ListView,
    ) {
        if (ldCategoryName.value == "Штучные товары") {
            val weightAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, ldShop.weightProductVitrin)
            ldCategory.value = weightAdapter
            lv.adapter = ldCategory.value
            ldCategoryName.value = "Весовые товары"
        } else {
            val counterAdapter = ArrayAdapter(context, R.layout.simple_list_item_1, ldShop.countableProductVitrin)
            ldCategory.value = counterAdapter
            lv.adapter = ldCategory.value
            ldCategoryName.value = "Штучные товары"
        }
        ldProduct.value = "Продукт"
    }

    fun choice(
        position: Int,
        lv: ListView,
        flag: Boolean
    ) {
        val adapter = lv.adapter as ArrayAdapter<String>
        if (flag) {
            ldProduct.value = adapter.getItem(position)
        } else {
            ldProductInBascket.value = adapter.getItem(position)
        }

    }


}