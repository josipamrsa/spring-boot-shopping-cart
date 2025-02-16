package com.example.shoppingcart.repositories

import com.example.shoppingcart.model.CartItem
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.time.Instant

interface CartItemRepository: MongoRepository<CartItem, String> {
    fun findByCartItemId(cartItemId: ObjectId): CartItem

    @Query("{ 'purchasedAt': { \$gte: ?0, \$lte: ?1 } }")
    fun findBetweenStartAndEndPeriod(start: Instant, end: Instant) : List<CartItem>
}