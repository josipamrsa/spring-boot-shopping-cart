package com.example.shoppingcart.controllers

import com.example.shoppingcart.model.CartItem
import com.example.shoppingcart.model.enums.Action
import com.example.shoppingcart.request.cart_item.ShoppingCartItemRequest
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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
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

    @Operation(summary = "Creates an empty shopping cart.")
    @PostMapping("/create")
    fun createShoppingCart(): ResponseEntity<ShoppingCartResponse> {
        return try {
            ResponseEntity.ok(
                shoppingCartService.createShoppingCart().toShoppingCartResponse()
            )
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @Operation(summary = "Fetches all items in a shopping cart with a provided ID value if it contains any. Otherwise, it returns an empty response.")
    @GetMapping("/{cartId}")
    fun fetchAllItemsInCart(
        @Parameter(name = "cartId", description = "Unique cart identifier", example = "1")
        @PathVariable cartId: String,
    ): ResponseEntity<List<ShoppingCartItemResponse>> {
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

    @Operation(summary = "Adds a new item to a shopping cart.")
    @PatchMapping("/add_item/{cartId}")
    fun addItemToCart(
        @Parameter(name = "cartId", description = "Unique cart identifier", example = "1")
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

    @Operation(summary = "Removes an item from a shopping cart.")
    @PatchMapping("/remove_item/{cartId}")
    fun removeItemFromCart(
        @Parameter(name = "cartId", description = "Unique cart identifier", example = "1")
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

    @Operation(summary = "Clears all added items in the shopping cart.")
    @PatchMapping("/clear/{cartId}")
    fun clearCart(
        @Parameter(name = "cartId", description = "Unique cart identifier", example = "1")
        @PathVariable cartId: String,
    ): ResponseEntity<String> {
        return try {
            shoppingCartService.clearShoppingCart(cartId)
            ResponseEntity.ok("Your shopping cart is cleared!")

        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Makes a purchase of all items listed in the shopping cart. Items being purchased are assigned an ADD action to their action field.")
    @PostMapping("/purchase/{cartId}")
    fun purchaseItems(
        @Parameter(name = "cartId", description = "Unique cart identifier", example = "1")
        @PathVariable cartId: String
    ): ResponseEntity<String> {
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

    @Operation(summary = "Fetches all of the already purchased items by cartId.")
    @GetMapping("/purchased_items/{cartId}")
    fun fetchPurchasedItems(
        @Parameter(name = "cartId", description = "Unique cart item identifier", example = "1")
        @PathVariable cartId: String,
    ): ResponseEntity<List<PurchasedCartItemResponse>> {
        return try {
            val items = cartItemService
                .fetchPurchasedItems(cartId)
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

    @Operation(summary = "Upgrades an item that was already purchased. Items being upgraded are assigned a MODIFY action to their action field.")
    @PatchMapping("/upgrade_item/{cartItemId}")
    fun upgradeItem(
        @Parameter(name = "cartItemId", description = "Unique cart item identifier", example = "1")
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        return try {
            // If no relatedCartId provided, then it's a bad request
            if (request.relatedCartId == null)
                return ResponseEntity.badRequest().build()
            else {
                cartItemService.upgradeItem(cartItemId, request.relatedCartId)
                return ResponseEntity.ok("Item successfully upgraded!")
            }

        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(summary = "Cancels an item that was already purchased (like a subscription). Items being upgraded are assigned a DELETE action to their action field.")
    @PatchMapping("/cancel_item/{cartItemId}")
    fun cancelItem(
        @Parameter(name = "cartItemId", description = "Unique cart item identifier", example = "1")
        @PathVariable cartItemId: String,
        @RequestBody request: ShoppingCartItemRequest
    ): ResponseEntity<String> {
        return try {
            // If no relatedCartId provided, then it's a bad request
            if (request.relatedCartId == null)
                return ResponseEntity.badRequest().build()
            else {
                cartItemService.cancelItem(cartItemId, request.relatedCartId)
                ResponseEntity.ok("Item successfully canceled!")
            }
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        } catch (e: NotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @Operation(
        summary = "Fetches purchase statistics from already purchased items (how many items were purchased in specified time period). " +
                "Date of the purchase corresponds to the date of creation of item in the database." +
                "Start date should never be greater than end date, otherwise a Bad Request (400) status is returned."
    )
    @PostMapping("/statistics")
    fun fetchSoldItemsInTimePeriod(
        @RequestBody request: TimePeriodRequest
    ): ResponseEntity<List<PurchasedCartItemResponse>> {
        return try {
            if (request.startPeriod > request.endPeriod)
                return ResponseEntity.badRequest().build()

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