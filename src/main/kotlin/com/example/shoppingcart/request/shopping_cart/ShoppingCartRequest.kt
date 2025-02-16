package com.example.shoppingcart.request.shopping_cart

import com.example.shoppingcart.request.shopping_cart_item.ShoppingCartItemRequest

data class ShoppingCartRequest(
    val cartId: String,
    val cartItems: List<ShoppingCartItemRequest>
)