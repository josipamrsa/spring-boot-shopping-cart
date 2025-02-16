package com.example.shoppingcart.model

import com.example.shoppingcart.model.enums.Action
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("cartitems")
data class CartItem(
    @Id val cartItemId: ObjectId = ObjectId(),
    val relatedCartId: String,
    val offerId: String,
    val action: Action?,
    val price: Price
)