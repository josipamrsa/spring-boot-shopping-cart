package com.example.shoppingcart.request.cart_item

import com.example.shoppingcart.model.Price
import com.example.shoppingcart.model.enums.Action

data class ShoppingCartItemRequest(
    val relatedCartId: String?,
    val offerId: String,
    val action: Action?,
    val price: Price
)

data class PurchasedCartItemRequest(
    val cartItemId: String,
    val relatedCartId: String?,
    val offerId: String,
    val action: Action?,
    val price: Price
)


