package com.example.shoppingcart.request.shopping_cart_item

import com.example.shoppingcart.model.Price
import com.example.shoppingcart.model.enums.Action

data class ShoppingCartItemRequest(
    val offerId: String,
    //val action: Action,
    val price: Price
)

