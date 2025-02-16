package com.example.shoppingcart.response.cart_item

import com.example.shoppingcart.model.CartItem

fun ShoppingCartItemResponse.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId
)

fun CartItem.toShoppingCartItemResponse() = ShoppingCartItemResponse(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId
)