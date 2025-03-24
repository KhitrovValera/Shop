package com.example.myapplication

class Shop private constructor() {

    val countableProducts: MutableList<Product> = mutableListOf(
        Product.CountableProduct("Бутылка воды", 50F),
        Product.CountableProduct("Мука 1кг", 40F),
        Product.CountableProduct("Сахар 1кг", 60F),
        Product.CountableProduct("Соус такой-то", 120F),
        Product.CountableProduct("Батон 500г", 40F),
        Product.CountableProduct("Колбаса 500г", 300F),
        Product.CountableProduct("Сыр 200г", 250F),
        Product.CountableProduct("Шоколад 100г", 90F),
        Product.CountableProduct("Мыло", 50F),
        Product.CountableProduct("Зубная паста", 90F),
    )

    val countableProductVitrin: List<String> = countableProducts.map { product ->
        "${product.name} \n${product.price} руб"
    }

    val weightProducts: MutableList<Product> = mutableListOf(
        Product.CountableProduct("Бананы", 100F),
        Product.WeighableProduct("Яблоки", 80F),
        Product.WeighableProduct("Персики", 95F),
        Product.WeighableProduct("Клубника", 150F),
        Product.WeighableProduct("Груши", 90F),
        Product.WeighableProduct("Картофель", 40F),
        Product.WeighableProduct("Огурцы", 70F),
        Product.WeighableProduct("Помидоры", 80F),
        Product.WeighableProduct("Лук", 50F),
        Product.WeighableProduct("Морковка", 60F),
    )

    val weightProductVitrin: List<String> = weightProducts.map { product ->
        "${product.name} \n${product.price} руб"
    }

    fun toProduct(stringProduct: String, quantity: Int): Product {
        val parts = stringProduct.split("\n")
        val name = parts[0].trim()
        val priceString = parts[1].split(" ")[0]
        val price = priceString.toFloat()
        return ProductFactory.createCountableProduct(name, price, quantity)
    }

    fun toProduct(stringProduct: String, weight: Float): Product {
        val parts = stringProduct.split("\n")
        val name = parts[0].trim()
        val priceString = parts[1].split(" ")[0]
        val price = priceString.toFloat()
        return ProductFactory.createWeighableProduct(name, price, weight)
    }

    fun parseProductFromString(productString: String): Product? {
        val countableProductRegex = Regex("""(.+?) \n([\d.]+) руб ([\d.]+) штук""")
        val weighableProductRegex = Regex("""(.+?) \n([\d.]+) руб ([\d.]+) кг""")

        val countableMatch = countableProductRegex.matchEntire(productString)
        val weighableMatch = weighableProductRegex.matchEntire(productString)

        return when {
            countableMatch != null -> {
                val (name, price, quantity) = countableMatch.destructured
                ProductFactory.createCountableProduct(name, price.toFloat(), quantity.toInt())
            }
            weighableMatch != null -> {
                val (name, price, weight) = weighableMatch.destructured
                ProductFactory.createWeighableProduct(name, price.toFloat(), weight.toFloat())
            }
            else -> null
        }
    }

    companion object {
        @Volatile private var instance: Shop? = null

        fun getInstance(): Shop =
            instance ?: synchronized(this) {
                instance ?: Shop().also { instance = it }
            }
    }
}
