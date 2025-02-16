package com.example.shoppingcart.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("shoppingcarts")
data class ShoppingCart(
    @Id val cartId: ObjectId = ObjectId(),
    val cartItems: List<CartItem> = listOf()
)