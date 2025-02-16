package com.example.shoppingcart.repositories

import com.example.shoppingcart.model.CartItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface CartItemRepository: MongoRepository<CartItem, String> {
    fun findByCartItemId(cartItemId: ObjectId): CartItem
    fun findByOfferId(offerId: String): List<CartItem>
}