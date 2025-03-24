package com.example.myapplication

class User(var cardMoney: Float, var nalMoney: Float, var bonusMoney: Float) {

    var basket: MutableList<Product> = mutableListOf()
    var basketString: List<String> = listOf()


    private fun updateBasketString() {
        basketString = basket.map {
            when (it) {
                is Product.CountableProduct -> "${it.name} \n${it.price} руб ${it.quantity} штук"
                is Product.WeighableProduct -> "${it.name} \n${it.price} руб ${it.weight} кг"
                else -> ""
            }
        }
    }

    fun clearBasket() {
        basket = mutableListOf()
        updateBasketString()
    }

    fun addToBasket(product: Product, quantity: Int = 0, weight: Float = 0F) {
        if (quantity < 0 || weight < 0F) {
            println("Количество и вес не могут быть отрицательными.")
            return
        }
        if (quantity == 0 && weight == 0F) {
            println("Количество и вес не могут быть равны нулю.")
            return
        }

        val existingProductIndex = basket.indexOfFirst {
            it.name == product.name
        }

        if (existingProductIndex != -1) {
            val existingProduct = basket[existingProductIndex]
            when (existingProduct) {
                is Product.CountableProduct -> {
                    val updatedQuantity = existingProduct.quantity + quantity
                    basket[existingProductIndex] = existingProduct.copy(quantity = updatedQuantity)
                }
                is Product.WeighableProduct -> {
                    val updatedWeight = existingProduct.weight + weight
                    basket[existingProductIndex] = existingProduct.copy(weight = updatedWeight)
                }
            }
        } else {
            val productToAdd = when (product) {
                is Product.CountableProduct -> product.copy(quantity = quantity)
                is Product.WeighableProduct -> product.copy(weight = weight)
            }
            basket.add(productToAdd)
        }
        updateBasketString()
    }

    fun removeFromBasket(product: Product, quantity: Int = 0, weight: Float = 0F) {
        if (quantity < 0 || weight < 0F) {
            println("Количество и вес не могут быть отрицательными.")
            return
        }
        if (quantity == 0 && weight == 0F) {
            println("Количество и вес не могут быть равны нулю.")
            return
        }

        val existingProductIndex = basket.indexOfFirst {
            it.name == product.name
        }

        if (existingProductIndex != -1) {
            val existingProduct = basket[existingProductIndex]
            when (existingProduct) {
                is Product.CountableProduct -> {
                    val updatedQuantity = existingProduct.quantity - quantity
                    if (updatedQuantity <= 0) {
                        basket.removeAt(existingProductIndex)
                    } else {
                        basket[existingProductIndex] = existingProduct.copy(quantity = updatedQuantity)
                    }
                }
                is Product.WeighableProduct -> {
                    val updatedWeight = existingProduct.weight - weight
                    if (updatedWeight <= 0F) {
                        basket.removeAt(existingProductIndex)
                    } else {
                        basket[existingProductIndex] = existingProduct.copy(weight = updatedWeight)
                    }
                }
            }
            updateBasketString()
        } else {
            println("Продукт не найден в корзине.")
        }
    }


    fun priceInBascket(): Float {
        var price = 0F
        for (product in basket) {
            price += product.calculateTotalCost()
        }
        return price
    }

    fun buy(
        price: Float,
        cardValue: Float = 0.0f,
        nalValue: Float = 0.0f,
        bonusValue: Float = 0.0f
    ): Boolean {
        return if (price > 0 && cardMoney >= cardValue && nalMoney >= nalValue &&
            bonusMoney >= bonusValue && (price - (cardValue + nalValue + bonusValue) == 0f)) {
            cardMoney -= cardValue
            nalMoney -= nalValue
            bonusMoney = bonusMoney - bonusValue + price * 0.05.toFloat()
            true
        } else false
    }
}