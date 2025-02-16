package com.example.shoppingcart.response.shopping_cart

import com.example.shoppingcart.model.ShoppingCart
import com.example.shoppingcart.response.shopping_cart_item.toCartItem
import com.example.shoppingcart.response.shopping_cart_item.toShoppingCartItemResponse
import org.bson.types.ObjectId

fun ShoppingCartResponse.toShoppingCart() = ShoppingCart(
    cartId = ObjectId(cartId),
    cartItems = cartItems.map { it.toCartItem() }
)

fun ShoppingCart.toShoppingCartResponse() = ShoppingCartResponse(
    cartId = cartId.toString(),
    cartItems = cartItems.map { it.toShoppingCartItemResponse() }
)

