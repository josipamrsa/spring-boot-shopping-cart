package com.example.shoppingcart.response.cart_item

import com.example.shoppingcart.model.CartItem
import org.bson.types.ObjectId

fun ShoppingCartItemResponse.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
)

fun CartItem.toShoppingCartItemResponse() = ShoppingCartItemResponse(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
)

fun PurchasedCartItemResponse.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
    cartItemId = ObjectId(cartItemId)
)

fun CartItem.toPurchasedCartItemResponse() = PurchasedCartItemResponse(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
    cartItemId = cartItemId.toString()
)