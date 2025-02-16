package com.example.shoppingcart.request.shopping_cart

import com.example.shoppingcart.model.ShoppingCart
import com.example.shoppingcart.request.shopping_cart_item.toCartItem
import com.example.shoppingcart.request.shopping_cart_item.toShoppingCartItemRequest
import org.bson.types.ObjectId

fun ShoppingCartRequest.toShoppingCart() = ShoppingCart(
    cartId = ObjectId(cartId),
    cartItems = cartItems.map { it.toCartItem() }
)

fun ShoppingCart.toShoppingCartRequest() = ShoppingCartRequest(
    cartId = cartId.toString(),
    cartItems = cartItems.map { it.toShoppingCartItemRequest() }
)