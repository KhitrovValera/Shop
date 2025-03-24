package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            with(mainViewModel) {
                ldCategoryName.observe(this@MainActivity) {
                    tvCat.text = it
                }
                ldCategory.observe(this@MainActivity) {
                    lvCounteble.adapter = it
                }
                ldProduct.observe(this@MainActivity) {
                    tvProduct.text = it
                }
                ldBascket.observe(this@MainActivity) {
                    lvBascket.adapter = it
                }
                ldProductInBascket.observe(this@MainActivity) {
                    tvProductInBascket.text = it
                }
                ldBalance.observe(this@MainActivity) {
                    tvBalance.text = it
                    tvBalance2.text = it
                }
                ldVisability.observe(this@MainActivity) {
                    gCatalog.isVisible = it
                    gPayment.isVisible = !it
                }
                ldPrice.observe(this@MainActivity) {
                    tvPrice.text = "$it руб"
                    tvPrice2.text = "$it руб"
                }
            }
        }


        with(binding) {
            with(mainViewModel) {
                lvCounteble.setOnItemClickListener { _, _, position, _ ->
                    choice(position, lvCounteble, true)
                }
                lvBascket.setOnItemClickListener { _, _, position, _ ->
                    choice(position, lvBascket, false)
                }

                sCat.setOnClickListener {
                    swap(this@MainActivity, lvCounteble)
                }
                btnAdd.setOnClickListener {
                    add(this@MainActivity, lvBascket, edNember, tvProduct)
                }
                btnRemove.setOnClickListener {
                    remove(this@MainActivity, lvBascket, edNumber, tvProductInBascket)
                }
                btnClear.setOnClickListener {
                    clear(this@MainActivity)
                }
                btnBuy.setOnClickListener {
                    switch()
                }
                btnBack.setOnClickListener {
                    switch()
                }
                btnFinalBuy.setOnClickListener {
                    buy(this@MainActivity, edCredit, edNal, edBonus)
                }


            }
        }
    }


}