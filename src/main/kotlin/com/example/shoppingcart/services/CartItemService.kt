package com.example.shoppingcart.services

import com.example.shoppingcart.model.CartItem
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.repositories.CartItemRepository
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toCartItem
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CartItemService(
    private val cartItemRepository: CartItemRepository
) {
    /**
     * Fetches all purchased items by provided cartId.
     * @param cartId Identifier for existing shopping cart
     */
    fun fetchPurchasedItems(cartId: String) : List<CartItem> =
        cartItemRepository.findByRelatedCartId(cartId)

    /**
     * Cancels an item that was already purchased (for recurring purchases, such as a subscription).
     * @param cartItems All cart items to save.
     */
    fun saveOrUpdateAllCartItems(cartItems: List<ShoppingCartItemRequest>): List<CartItem> =
        cartItemRepository.saveAll(cartItems.map { item ->
            item
                .toCartItem()
                .copy(purchasedAt = Instant.now())
        })

    /**
     * Upgrades an item that was already purchased (for recurring purchases, such as a subscription).
     * @param cartItemId Identifier for the cart item
     * @param cartId Identifier for existing shopping cart
     */
    fun upgradeItem(cartItemId: String, cartId: String) {
        val itemToUpgrade = cartItemRepository.findByCartItemId(ObjectId(cartItemId))
        cartItemRepository.save(itemToUpgrade.copy(action = Action.MODIFY))
    }

    /**
     * Cancels an item that was already purchased (for recurring purchases, such as a subscription).
     * @param cartItemId Identifier for the cart item
     * @param cartId Identifier for existing shopping cart
     */
    fun cancelItem(cartItemId: String, cartId: String) {
        val itemToCancel = cartItemRepository.findByCartItemId(ObjectId(cartItemId))
        cartItemRepository.save(itemToCancel.copy(action = Action.DELETE))
    }

    /**
     * Fetches item statistics, i.e. items that were purchased in a specified time period.
     * @param startPeriod Start date
     * @param endPeriod End date
     */
    fun fetchItemStatistics(startPeriod: Instant, endPeriod: Instant) =
        cartItemRepository.findBetweenStartAndEndPeriod(startPeriod, endPeriod)
}