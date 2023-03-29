package com.tamayo.amazonrobotics

import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AmazonShoppingCartTestMockK {

    private val mockCart = AmazonShoppingCart()


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
    }

    @After
    fun onAfter() {
        clearAllMocks()
    }

    @Test
    fun `Add a new item in the Mutable List of items`() {

        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        mockCart.add(laptop)
        mockCart.add(guitar)


        assert(mockCart.items.size == 2)

    }

    @Test
    fun `Remove a item in the Mutable List of items`() {

        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        mockCart.add(laptop)
        mockCart.add(guitar)
        mockCart.remove(laptop)

        assert(mockCart.items.size == 1)

    }


    @Test
    fun `Get total Price without discount and taxes`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal =
            mockCart.calculateFinalTotal(stateTaxPercent = 0.0, federalTaxPercent = 0.0)

        // Assert final total
        assertEquals(800.0, finalTotal, 0.0)
    }

    @Test
    fun `Final Total whit Federal Tax but without State Tax`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0
        every { guitar.discount } returns 50.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal =
            mockCart.calculateFinalTotal(stateTaxPercent = 6.0, federalTaxPercent = 0.0)

        // Assert final total
        assertEquals(689.0, finalTotal, 0.0)
    }

    @Test
    fun `Final Total whit State Tax but without Federal Tax`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0
        every { guitar.discount } returns 50.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal =
            mockCart.calculateFinalTotal(stateTaxPercent = 0.0, federalTaxPercent = 10.0)

        // Assert final total
        assertEquals(715.0, finalTotal, 0.0)
    }

    @Test
    fun `Final Total whit State Tax, Federal Tax and Discount`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0
        every { guitar.discount } returns 50.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal =
            mockCart.calculateFinalTotal(stateTaxPercent = 6.0, federalTaxPercent = 10.0)

        // Assert final total
        assertEquals(754.0, finalTotal, 0.0)
    }

    @Test
    fun `Final Total whit State Tax, Federal Tax but without Discount`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal =
            mockCart.calculateFinalTotal(stateTaxPercent = 6.0, federalTaxPercent = 10.0)

        // Assert final total
        assertEquals(928.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Federal Tax using the Sub Total but without Discount`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateStateTax(taxPercent = 6.0)

        // Assert final total
        assertEquals(48.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate State Tax using the Sub Total but without Discount`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateStateTax(taxPercent = 10.0)

        // Assert final total
        assertEquals(80.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Sub Total but without Discount`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateSubTotal()

        // Assert final total
        assertEquals(800.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Sub Total when discount is added in one item`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { laptop.discount } returns 30.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateSubTotal()

        // Assert final total
        assertEquals(650.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Sub Total when discount is added in two item`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { laptop.discount } returns 30.0
        every { guitar.price } returns 300.0
        every { guitar.discount } returns 50.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateSubTotal()

        // Assert final total
        assertEquals(500.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Total discount is added in one items`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { laptop.discount } returns 30.0
        every { guitar.price } returns 300.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateDiscount()

        // Assert final total
        assertEquals(150.0, finalTotal, 0.0)
    }

    @Test
    fun `Calculate Total discount is added in two items`() {
        // Create mock items
        val laptop = mockk<Item>(relaxed = true)
        val guitar = mockk<Item>(relaxed = true)

        // Mock item properties
        every { laptop.price } returns 500.0
        every { laptop.discount } returns 30.0
        every { guitar.price } returns 300.0
        every { guitar.discount } returns 50.0

        // Add items to cart
        mockCart.add(laptop)
        mockCart.add(guitar)

        // Calculate final total
        val finalTotal = mockCart.calculateDiscount()

        // Assert final total
        assertEquals(300.0, finalTotal, 0.0)
    }


}