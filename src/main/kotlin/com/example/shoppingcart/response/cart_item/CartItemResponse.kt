package com.example.shoppingcart.response.cart_item

import com.example.shoppingcart.model.Price
import com.example.shoppingcart.model.enums.Action

data class ShoppingCartItemResponse(
    val offerId: String,
    val price: Price,
    val action: Action?,
    val relatedCartId: String?
)
