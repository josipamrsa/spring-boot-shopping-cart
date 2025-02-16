package com.example.shoppingcart.request.shopping_cart_item

import com.example.shoppingcart.model.CartItem

fun ShoppingCartItemRequest.toCartItem() = CartItem(
    offerId = offerId,
    action = action,
    price = price
)

fun CartItem.toShoppingCartItemRequest() = ShoppingCartItemRequest(
    offerId = offerId,
    action = action,
    price = price
)