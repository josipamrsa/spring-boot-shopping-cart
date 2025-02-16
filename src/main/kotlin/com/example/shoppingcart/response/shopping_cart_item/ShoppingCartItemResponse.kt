package com.example.shoppingcart.response.shopping_cart_item

import com.example.shoppingcart.model.Price
import com.example.shoppingcart.model.enums.Action

data class ShoppingCartItemResponse(
    val offerId: String,
    //val action: Action,
    val price: Price
)