package com.example.shoppingcart.controllers

import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.request.shopping_cart.ShoppingCartRequest
import com.example.shoppingcart.request.shopping_cart.toShoppingCart
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toShoppingCartItemRequest
import com.example.shoppingcart.response.shopping_cart.ShoppingCartResponse
import com.example.shoppingcart.response.shopping_cart.toShoppingCartResponse
import com.example.shoppingcart.services.CartItemService
import com.example.shoppingcart.services.ShoppingCartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingCartController(
    private val shoppingCartService: ShoppingCartService,
    private val cartItemService: CartItemService
) {
    @PostMapping
    fun createShoppingCart() =
        ResponseEntity.ok(
            shoppingCartService.createShoppingCart().toShoppingCartResponse()
        )

    @GetMapping("/{cartId}")
    fun fetchAllItemsInCart(@PathVariable cartId: String) =
        ResponseEntity.ok(
            shoppingCartService.fetchAllCartItems(cartId)
        )

    @PatchMapping("/add_item/{cartId}")
    fun addItemToCart(@PathVariable cartId: String, @RequestBody request: ShoppingCartItemRequest) {
        ResponseEntity.ok(
            shoppingCartService.addItemToCart(cartId, request)
        )
    }

    @PatchMapping("/remove_item/{cartId}")
    fun removeItemFromCart(@PathVariable cartId: String, @RequestBody request: ShoppingCartItemRequest) {
        ResponseEntity.ok(
            shoppingCartService.removeItemFromCart(cartId, request)
        )
    }

    @PostMapping("/purchase/{cartId}")
    fun purchaseItems(@PathVariable cartId: String) {
        val cartItems = shoppingCartService.fetchAllCartItems(cartId)

        cartItemService.saveOrUpdateAllCartItems(cartItems.map { it.toShoppingCartItemRequest().copy(
            action = it.action ?: Action.ADD,
            relatedCartId = cartId
        ) })

        shoppingCartService.clearShoppingCart(cartId)

        ResponseEntity.ok(
            "items purchased"
        )
    }

    @PatchMapping("/upgrade_item/{cartItemId}")
    fun upgradeRecurringItem(@PathVariable cartItemId: String, @RequestBody request: ShoppingCartItemRequest) {
        cartItemService.upgradeRecurringItem(cartItemId, request.relatedCartId)
    }

    @PatchMapping("/cancel_item/{cartItemId}")
    fun cancelRecurringItem(@PathVariable cartItemId: String, @RequestBody request: ShoppingCartItemRequest) {
        cartItemService.cancelRecurringItem(cartItemId, request.relatedCartId)
    }
}