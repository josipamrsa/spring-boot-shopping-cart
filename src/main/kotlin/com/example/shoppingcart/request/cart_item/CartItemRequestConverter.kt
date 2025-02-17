package com.example.shoppingcart.request.cart_item

import com.example.shoppingcart.model.CartItem
import org.bson.types.ObjectId

fun ShoppingCartItemRequest.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
)

fun CartItem.toShoppingCartItemRequest() = ShoppingCartItemRequest(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
)

fun PurchasedCartItemRequest.toCartItem() = CartItem(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
    cartItemId = ObjectId(cartItemId)
)

fun CartItem.toPurchasedCartItemRequest() = PurchasedCartItemRequest(
    offerId = offerId,
    price = price,
    action = action,
    relatedCartId = relatedCartId,
    cartItemId = cartItemId.toString()
)

