package com.example.shoppingcart.services

import com.example.shoppingcart.model.ShoppingCart
import com.example.shoppingcart.repositories.ShoppingCartRepository
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toCartItem
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ShoppingCartService(
    private val shoppingCartRepository: ShoppingCartRepository
) {
    /**
     * Creates a new shopping cart.
     */
    fun createShoppingCart() = shoppingCartRepository.save(ShoppingCart())

    /**
     * Fetches all cart items currently added to a cart.
     * @param cartId Identifier for existing shopping cart
     */
    fun fetchAllCartItems(cartId: String) =
        shoppingCartRepository.findByCartId(ObjectId(cartId)).cartItems

    /**
     * Adds a new item to the cart.
     * @param cartId Identifier for existing shopping cart
     * @param cartItem Cart item to add
     */
    fun addItemToCart(cartId: String, cartItem: ShoppingCartItemRequest) {
        val currentShoppingCart = shoppingCartRepository.findByCartId(ObjectId(cartId))
        val updatedCartItems = currentShoppingCart.cartItems.toMutableList().plus(
            cartItem.toCartItem()
        )

        val updatedShoppingCart = currentShoppingCart.copy(
            cartItems = updatedCartItems
        )

        shoppingCartRepository.save(updatedShoppingCart)
    }

    /**
     * Removes an item from the cart.
     * @param cartId Identifier for existing shopping cart
     * @param cartItem Cart item to remove
     */
    fun removeItemFromCart(cartId: String, cartItem: ShoppingCartItemRequest) {
        val currentShoppingCart = shoppingCartRepository.findByCartId(ObjectId(cartId))
        val updatedCartItems = currentShoppingCart.cartItems.toMutableList().minus(
            cartItem.toCartItem()
        )

        val updatedShoppingCart = currentShoppingCart.copy(
            cartItems = updatedCartItems
        )

        shoppingCartRepository.save(updatedShoppingCart)
    }

    /**
     * Clears all items from the shopping cart.
     * @param cartId Identifier for existing shopping cart
     */
    fun clearShoppingCart(cartId: String): ShoppingCart {
        val currentCart = shoppingCartRepository.findByCartId(ObjectId(cartId))
        return shoppingCartRepository.save(currentCart.copy(cartItems = listOf()))
    }

}