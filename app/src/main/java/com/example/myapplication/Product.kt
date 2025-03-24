package com.example.myapplication


sealed class Product() {

    abstract val name: String
    abstract val price: Float

    data class CountableProduct(
        override val name: String,
        override val price: Float,
        val quantity: Int = 0
    ) : Product() {
        override fun calculateTotalCost(): Float {
            return price * quantity
        }
    }

    data class WeighableProduct(
        override val name: String,
        override val price: Float,
        val weight: Float = 0F
    ) : Product() {
        override fun calculateTotalCost(): Float {
            return price * weight
        }
    }

    abstract fun calculateTotalCost(): Float
}

