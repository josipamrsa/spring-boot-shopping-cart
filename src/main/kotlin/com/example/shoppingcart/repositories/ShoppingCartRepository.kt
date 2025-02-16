package com.example.shoppingcart.repositories

import com.example.shoppingcart.model.ShoppingCart
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ShoppingCartRepository: MongoRepository<ShoppingCart, String> {
    fun findByCartId(cartId: ObjectId) : ShoppingCart
}