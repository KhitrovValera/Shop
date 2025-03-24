package com.example.myapplication

object ProductFactory {

    fun createCountableProduct(name: String, price: Float, quantity: Int): Product.CountableProduct {
        return Product.CountableProduct(name, price, quantity)
    }

    fun createWeighableProduct(name: String, price: Float, weight: Float): Product.WeighableProduct {
        return Product.WeighableProduct(name, price, weight)
    }
}
