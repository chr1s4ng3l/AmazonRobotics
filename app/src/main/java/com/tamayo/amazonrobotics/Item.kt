package com.tamayo.amazonrobotics

/**
 * Represents an [Item] for sale in the Amazon shopping cart.
 *
 * @property id the unique identifier for the item
 * @property name the name of the item
 * @property price the price of the item in dollars
 * @property discount the discount amount for the item as a percentage (default is 0.0)
 */
data class Item(
    val id: String,
    val name: String,
    val price: Double,
    val discount: Double = 0.0
)

/**
 * Represents an Amazon shopping cart.
 *
 * @property items the list of items in the shopping cart
 * @constructor Creates an Amazon shopping cart with an optional list of items
 */
class AmazonShoppingCart(val items: MutableList<Item> = mutableListOf()) {

    /**
     * Adds an [Item] to the shopping cart.
     *
     * @param item The item to be added to the cart.
     */
    fun add(item: Item) {
        items.add(item)
    }

    /**
     * Removes an [Item] from the shopping cart.
     *
     * @param item The item to be removed from the cart.
     */
    fun remove(item: Item) {
        items.remove(item)
    }

    /**
     * Calculates the subtotal of all items in the shopping cart, minus any discounts.
     *
     * @return the subtotal
     */
    fun calculateSubTotal(): Double = items.sumOf { it.price } - calculateDiscount()

    /**
     * Calculates the federal tax for the items in the shopping cart.
     *
     * @param taxPercent the tax rate as a percentage
     * @return the federal tax amount
     */
    fun calculateFederalTax(taxPercent: Double): Double =
        if (taxPercent == 0.0) taxPercent else taxPercent / 100 * calculateSubTotal()

    /**
     * Calculates the state tax for the items in the shopping cart.
     *
     * @param taxPercent the tax rate as a percentage
     * @return the state tax amount
     */
    fun calculateStateTax(taxPercent: Double): Double =
        if (taxPercent == 0.0) taxPercent else taxPercent / 100 * calculateSubTotal()

    /**
     * Calculates the final total, including any discounts and taxes, for the items in the shopping cart.
     *
     * @param federalTaxPercent the federal tax rate as a percentage (default is 0.0)
     * @param stateTaxPercent the state tax rate as a percentage (default is 0.0)
     * @return the final total
     */
    fun calculateFinalTotal(
        federalTaxPercent: Double = 0.0, stateTaxPercent: Double = 0.0
    ): Double = calculateSubTotal() + calculateFederalTax(federalTaxPercent) + calculateStateTax(
        stateTaxPercent
    )

    /**
     * Calculates the total discount amount for all items in the shopping cart that have a discount.
     *
     * @return the total discount amount
     */
    fun calculateDiscount(): Double = items.filter { it.discount != 0.0 }.sumOf { item ->
        item.discount / 100 * item.price
    }

}

