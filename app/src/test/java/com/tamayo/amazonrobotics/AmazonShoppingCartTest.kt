package com.tamayo.amazonrobotics

import org.junit.Assert.*
import org.junit.Test

class AmazonShoppingCartTest {

    @Test
    fun `Add a new item in the Mutable List of items`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        assertEquals(2, cart.items.size)
    }

    @Test
    fun `Remove Item from the Mutable List of items`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)
        cart.remove(laptop)
        assertEquals(1, cart.items.size)

    }

    @Test
    fun `Get total Price without discount and taxes`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        assertEquals(
            800.00,
            cart.calculateFinalTotal(federalTaxPercent = 0.0, stateTaxPercent = 0.0),
            0.1
        )

    }

    @Test
    fun `Final Total with Federal Tax but without State Tax`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0, 50.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discounted prices Given 10% of State Tax
         (discount -> 50 / 100 * 300 = 150)
         (subTotal -> 800 - discount -> 150 = 650)
         (state tax -> 6 / 100 *  subTotal -> 650 = 39)
         (subTotal -> 650 + stateTac -> 39)
         should be $689.00
        */
        assertEquals(689.00, cart.calculateFinalTotal(federalTaxPercent = 6.0), 0.01)


    }

    @Test
    fun `Final Total whit State Tax but without Federal Tax`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0, 50.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discounted prices Given 10% of State Tax
         (discount -> 50 / 100 * 300 = 150)
         (subTotal -> 800 - discount -> 150 = 650)
         (state tax -> 10 / 100 *  subTotal -> 650 = 65)
         (subTotal -> 650 + stateTac -> 65)
         should be $715.00
         */
        assertEquals(715.00, cart.calculateFinalTotal(stateTaxPercent = 10.0), 0.01)

    }

    @Test
    fun `Final Total whit State Tax, Federal Tax and Discount`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0, 50.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discounted prices Given 10% of State Tax
         (discount -> 50 / 100 * 300 = 150)
         (subTotal -> 800 - discount -> 150 = 650)
         (state tax -> 10 / 100 *  subTotal -> 650 = 65)
         (federal tax -> 6 / 100 *  subTotal -> 650 = 39)
         (subTotal -> 650 + stateTax -> 65 + federalTax -> 39)
         should be $754.0
         */
        assertEquals(
            754.0,
            cart.calculateFinalTotal(federalTaxPercent = 6.0, stateTaxPercent = 10.0),
            0.01
        )

    }

    @Test
    fun `Final Total whit State Tax and Federal Tax but without Discount`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discounted prices Given 10% of State Tax
         (subTotal -> 800 - discount -> 150 = 650)
         (state tax -> 10 / 100 *  subTotal -> 800 = 80)
         (federal tax -> 6 / 100 *  subTotal -> 800 = 48)
         (subTotal -> 800 + stateTax -> 80 + federalTax -> 48)
         should be $928.0
         */
        assertEquals(
            928.0,
            cart.calculateFinalTotal(federalTaxPercent = 6.0, stateTaxPercent = 10.0),
            0.01
        )

    }

    @Test
    fun `Calculate Federal Tax using the Sub Total but without Discount`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
          Given 6% of Federal Tax (6 / 100 * 800) should be $48.00
         */
        assertEquals(48.00, cart.calculateFederalTax(taxPercent = 6.0), 0.01)

    }

    @Test
    fun `Calculate State Tax using the Sub Total but without Discount`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Given 10% of State Tax (10 / 100 * 800) should be $80.00
        */
        assertEquals(80.00, cart.calculateStateTax(taxPercent = 10.0), 0.01)

    }

    @Test
    fun `Calculate Sub Total but without Discount`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Given (laptop -> 500 + guitar -> 300 ) should be $800.00
        */
        assertEquals(800.00, cart.calculateSubTotal(), 0.01)

    }


    @Test
    fun `Calculate Sub Total when discount is added in one item`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0, 30.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discount -> 30 / 100 * laptop -> 500 = 150
         Given (laptop -> 500 + guitar -> 300 - discount -> 150) should be $650.00
        */
        assertEquals(650.00, cart.calculateSubTotal(), 0.01)

    }

    @Test
    fun `Calculate Sub Total when discount is added in two items`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0, 30.0)
        val guitar = Item("2222", "Guitar 1", 300.0, 50.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discount1 -> 30 / 100 * laptop -> 500 = 150
         Discount2 -> 50 / 100 * guitar -> 300 = 150
         discount = Discount1 + Discount2
         Given (laptop -> 500 + guitar -> 300 - discount -> 300) should be $500.00
        */
        assertEquals(500.00, cart.calculateSubTotal(), 0.01)

    }

    @Test
    fun `Calculate Total discount is added in one items`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0, 30.0)
        val guitar = Item("2222", "Guitar 1", 300.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discount1 -> 30 / 100 * laptop -> 500 = Should be 150
        */
        assertEquals(150.00, cart.calculateDiscount(), 0.01)

    }

    @Test
    fun `Calculate Total discount is added in two items`() {
        val cart = AmazonShoppingCart()
        val laptop = Item("1111", "Laptop 1", 500.0, 30.0)
        val guitar = Item("2222", "Guitar 1", 300.0, 50.0)
        cart.add(laptop)
        cart.add(guitar)

        /*
         Discount1 -> 30 / 100 * laptop -> 500 = 150
         Discount2 -> 50 / 100 * guitar -> 300 = 150
         discount = Discount1 + Discount2 = Should be 300
        */
        assertEquals(300.00, cart.calculateDiscount(), 0.01)

    }


}