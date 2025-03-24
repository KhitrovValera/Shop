package com.example.myapplication

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest=Config.NONE)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockContext = Mockito.mock(Context::class.java)
        viewModel = MainViewModel()
    }

    @Test
    fun testSwitch() {
        val initialVisibility = viewModel.ldVisability.value
        viewModel.switch()
        assertNotEquals(initialVisibility, viewModel.ldVisability.value)
    }
}

class ProductFactoryTest {

    @Test
    fun testCreateCountableProduct() {

        val factory = ProductFactory
        val name = "Apple"
        val price = 1.0f
        val quantity = 10


        val product = factory.createCountableProduct(name, price, quantity)


        assertNotNull(product)
        assertEquals(name, product.name)
        assertEquals(price, product.price, 0.01f)
        assertEquals(quantity, product.quantity)
    }

    @Test
    fun testCreateWeighableProduct() {

        val factory = ProductFactory
        val name = "Banana"
        val price = 0.5f
        val weight = 0.2f


        val product = factory.createWeighableProduct(name, price, weight)


        assertNotNull(product)
        assertEquals(name, product.name)
        assertEquals(price, product.price, 0.01f)
        assertEquals(weight, product.weight, 0.01f)
    }
}

class UserTest {

    private lateinit var user: User
    private lateinit var product1: Product.CountableProduct
    private lateinit var product2: Product.WeighableProduct

    @Before
    fun setUp() {
        user = User(100f, 50f, 10f)
        product1 = Product.CountableProduct("Product 1", 10f,)
        product2 = Product.WeighableProduct("Product 2", 8f, )
    }

    @Test
    fun testAddToBasketWithCountableProduct() {
        user.addToBasket(product1, quantity = 2)
        assertEquals(1, user.basket.size)
        assertEquals(20f, user.priceInBascket())
    }

    @Test
    fun testAddToBasketWithWeighableProduct() {
        user.addToBasket(product2, weight = 1.5f)
        assertEquals(1, user.basket.size)
        assertEquals(12f, user.priceInBascket())
    }

    @Test
    fun testAddToBasketWithNegativeQuantity() {
        user.addToBasket(product1, quantity = -2)
        assertTrue(user.basket.isEmpty())
    }
}

