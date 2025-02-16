package com.example.shoppingcart.services

import com.example.shoppingcart.model.CartItem
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.model.enums.PriceType
import com.example.shoppingcart.repositories.CartItemRepository
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toCartItem
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Service
class CartItemService(
    private val cartItemRepository: CartItemRepository
) {
    fun saveOrUpdateAllCartItems(request: List<ShoppingCartItemRequest>): List<CartItem> =
        cartItemRepository.saveAll(request.map { item ->
            item
                .toCartItem()
                .copy(purchasedAt = Instant.now())
        })

    fun upgradeRecurringItem(cartItemId: String, cartId: String?) {
        val itemToUpgrade = cartItemRepository.findByCartItemId(ObjectId(cartItemId))
        cartItemRepository.save(itemToUpgrade.copy(action = Action.MODIFY))
    }

    fun cancelRecurringItem(cartItemId: String, cartId: String?) {
        val itemToCancel = cartItemRepository.findByCartItemId(ObjectId(cartItemId))
        cartItemRepository.save(itemToCancel.copy(action = Action.DELETE))
    }
}