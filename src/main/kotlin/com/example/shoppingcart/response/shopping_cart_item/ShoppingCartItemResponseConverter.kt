package com.example.shoppingcart.response.shopping_cart_item

import com.example.shoppingcart.model.CartItem

fun ShoppingCartItemResponse.toCartItem() = CartItem(
    offerId = offerId,
    action = action,
    price = price
)

fun CartItem.toShoppingCartItemResponse() = ShoppingCartItemResponse(
    offerId = offerId,
    action = action,
    price = price
)