package com.example.shoppingcart.controllers

import com.example.shoppingcart.model.CartItem
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toShoppingCartItemRequest
import com.example.shoppingcart.request.time_period.TimePeriodRequest
import com.example.shoppingcart.response.shopping_cart.toShoppingCartResponse
import com.example.shoppingcart.services.CartItemService
import com.example.shoppingcart.services.ShoppingCartService
import org.springframework.http.ResponseEntity
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
    fun fetchAllItemsInCart(@PathVariable cartId: String): ResponseEntity<List<CartItem>> {
        val items = shoppingCartService.fetchAllCartItems(cartId)

        if (items.isEmpty())
            ResponseEntity.noContent().build<List<CartItem>>()

        return ResponseEntity.ok(items)
    }

    @PatchMapping("/add_item/{cartId}")
    fun addItemToCart(
        @PathVariable cartId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<List<CartItem>> {
        try {
            shoppingCartService.addItemToCart(cartId, request)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest()
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
        }

        val items = shoppingCartService.fetchAllCartItems(cartId)

        if (items.isEmpty())
            ResponseEntity.noContent().build<List<CartItem>>()

        return ResponseEntity.ok(items)
    }

    @PatchMapping("/remove_item/{cartId}")
    fun removeItemFromCart(
        @PathVariable cartId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<List<CartItem>> {
        try {
            shoppingCartService.removeItemFromCart(cartId, request)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest()
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
        }

        val items = shoppingCartService.fetchAllCartItems(cartId)

        if (items.isEmpty())
            ResponseEntity.noContent().build<List<CartItem>>()

        return ResponseEntity.ok(items)
    }

    @PostMapping("/purchase/{cartId}")
    fun purchaseItems(@PathVariable cartId: String): ResponseEntity<String> {
        val cartItems = shoppingCartService.fetchAllCartItems(cartId)

        if (cartItems.isEmpty())
            ResponseEntity.noContent().build<List<CartItem>>()

        try {
            cartItemService.saveOrUpdateAllCartItems(cartItems.map {
                it.toShoppingCartItemRequest().copy(
                    action = it.action ?: Action.ADD,
                    relatedCartId = cartId
                )
            })
            shoppingCartService.clearShoppingCart(cartId)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest()
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
        }

        return ResponseEntity.ok("Items successfully purchased!")
    }

    @PatchMapping("/upgrade_item/{cartItemId}")
    fun upgradeRecurringItem(
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        try {
            cartItemService.upgradeRecurringItem(cartItemId, request.relatedCartId)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest()
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
        }

        return ResponseEntity.ok("Item successfully upgraded!")
    }

    @PatchMapping("/cancel_item/{cartItemId}")
    fun cancelRecurringItem(
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        try {
            cartItemService.cancelRecurringItem(cartItemId, request.relatedCartId)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest()
        } catch (e: Exception) {
            ResponseEntity.internalServerError()
        }

        return ResponseEntity.ok("Item successfully canceled!")
    }

    @PostMapping("/statistics")
    fun fetchSoldItemsInTimePeriod(@RequestBody request: TimePeriodRequest): ResponseEntity<List<CartItem>> {
        val items = cartItemService.fetchItemStatistics(request.startPeriod, request.endPeriod)

        if (items.isEmpty())
            ResponseEntity.noContent().build<List<CartItem>>()

        return ResponseEntity.ok(items)
    }
}