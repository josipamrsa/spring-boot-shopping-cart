package com.example.shoppingcart.response.shopping_cart

import com.example.shoppingcart.response.shopping_cart_item.ShoppingCartItemResponse

data class ShoppingCartResponse(
    val cartId: String,
    val cartItems: List<ShoppingCartItemResponse>
)