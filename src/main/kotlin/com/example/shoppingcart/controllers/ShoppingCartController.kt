package com.example.shoppingcart.controllers

import com.example.shoppingcart.model.CartItem
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.request.cart_item.toPurchasedCartItemRequest
import com.example.shoppingcart.request.cart_item.toShoppingCartItemRequest
import com.example.shoppingcart.request.time_period.TimePeriodRequest
import com.example.shoppingcart.response.cart_item.PurchasedCartItemResponse
import com.example.shoppingcart.response.cart_item.ShoppingCartItemResponse
import com.example.shoppingcart.response.cart_item.toPurchasedCartItemResponse
import com.example.shoppingcart.response.cart_item.toShoppingCartItemResponse
import com.example.shoppingcart.response.shopping_cart.ShoppingCartResponse
import com.example.shoppingcart.response.shopping_cart.toShoppingCartResponse
import com.example.shoppingcart.services.CartItemService
import com.example.shoppingcart.services.ShoppingCartService
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
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
    fun createShoppingCart(): ResponseEntity<ShoppingCartResponse> {
        return try {
            ResponseEntity.ok(
                shoppingCartService.createShoppingCart().toShoppingCartResponse()
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/{cartId}")
    fun fetchAllItemsInCart(@PathVariable cartId: String): ResponseEntity<List<ShoppingCartItemResponse>> {
        return try {
            val items = shoppingCartService
                .fetchAllCartItems(cartId)
                .map { it.toShoppingCartItemResponse() }

            if (items.isEmpty())
                ResponseEntity.noContent().build()
            else ResponseEntity.ok(items)

        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/add_item/{cartId}")
    fun addItemToCart(
        @PathVariable cartId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<List<ShoppingCartItemResponse>> {
        return try {
            shoppingCartService.addItemToCart(cartId, request)
            val items = shoppingCartService
                .fetchAllCartItems(cartId)
                .map { it.toShoppingCartItemResponse() }

            if (items.isEmpty())
                ResponseEntity.noContent().build()
            else ResponseEntity.ok(items)

        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/remove_item/{cartId}")
    fun removeItemFromCart(
        @PathVariable cartId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<List<ShoppingCartItemResponse>> {
        return try {
            shoppingCartService.removeItemFromCart(cartId, request)

            val items = shoppingCartService
                .fetchAllCartItems(cartId)
                .map { it.toShoppingCartItemResponse() }

            if (items.isEmpty())
                ResponseEntity.noContent().build()
            else ResponseEntity.ok(items)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/purchase/{cartId}")
    fun purchaseItems(@PathVariable cartId: String): ResponseEntity<String> {
        return try {
            val cartItems = shoppingCartService.fetchAllCartItems(cartId)

            if (cartItems.isEmpty())
                ResponseEntity.noContent().build<List<CartItem>>()

            cartItemService.saveOrUpdateAllCartItems(cartItems.map {
                it.toShoppingCartItemRequest().copy(
                    action = it.action ?: Action.ADD,
                    relatedCartId = cartId
                )
            })

            shoppingCartService.clearShoppingCart(cartId)

            ResponseEntity.ok("Items successfully purchased!")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/upgrade_item/{cartItemId}")
    fun upgradeRecurringItem(
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        return try {
            cartItemService.upgradeRecurringItem(cartItemId, request.relatedCartId)
            ResponseEntity.ok("Item successfully upgraded!")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PatchMapping("/cancel_item/{cartItemId}")
    fun cancelRecurringItem(
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        return try {
            cartItemService.cancelRecurringItem(cartItemId, request.relatedCartId)
            ResponseEntity.ok("Item successfully canceled!")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/statistics")
    fun fetchSoldItemsInTimePeriod(@RequestBody request: TimePeriodRequest): ResponseEntity<List<PurchasedCartItemResponse>> {
        return try {
            val items = cartItemService
                .fetchItemStatistics(request.startPeriod, request.endPeriod)
                .map { it.toPurchasedCartItemResponse() }

            if (items.isEmpty())
                ResponseEntity.noContent().build()
            else ResponseEntity.ok(items)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }
}