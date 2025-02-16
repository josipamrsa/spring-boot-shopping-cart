package com.example.shoppingcart.request.cart_item

import com.example.shoppingcart.model.CartItem

fun ShoppingCartItemRequest.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId
)

fun CartItem.toShoppingCartItemRequest() = ShoppingCartItemRequest(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId
)

