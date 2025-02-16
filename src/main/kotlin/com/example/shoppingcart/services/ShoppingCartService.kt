package com.example.shoppingcart.services

import com.example.shoppingcart.model.ShoppingCart
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.repositories.ShoppingCartRepository
import com.example.shoppingcart.request.shopping_cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.shopping_cart_item.toCartItem
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    fun createShoppingCart() = shoppingCartRepository.save(ShoppingCart())

    fun fetchAllCartItems(cartId: String) =
        shoppingCartRepository.findByCartId(ObjectId(cartId)).cartItems

    fun addItemToCart(cartId: String, request: ShoppingCartItemRequest) {
        val currentShoppingCart = shoppingCartRepository.findByCartId(ObjectId(cartId))
        val updatedCartItems = currentShoppingCart.cartItems.toMutableList().plus(
            request.toCartItem()
        )

        val updatedShoppingCart = currentShoppingCart.copy(
            cartItems = updatedCartItems
        )

        shoppingCartRepository.save(updatedShoppingCart)
    }

    fun removeItemFromCart(cartId: String, request: ShoppingCartItemRequest) {
        val currentShoppingCart = shoppingCartRepository.findByCartId(ObjectId(cartId))
        val updatedCartItems = currentShoppingCart.cartItems.toMutableList().minus(
            request.toCartItem()
        )

        val updatedShoppingCart = currentShoppingCart.copy(
            cartItems = updatedCartItems
        )

        shoppingCartRepository.save(updatedShoppingCart)
    }

    fun deleteShoppingCart(shoppingCart: ShoppingCart) {
        shoppingCartRepository.delete(shoppingCart)
    }

    fun purchaseItems(shoppingCart: ShoppingCart) {
        val purchasedItems = shoppingCart.cartItems.map { item ->
            item.copy(action = item.action ?: Action.ADD)
        }

        val updatedCart = shoppingCart.copy(cartItems = purchasedItems)
        shoppingCartRepository.save(updatedCart)
    }
}