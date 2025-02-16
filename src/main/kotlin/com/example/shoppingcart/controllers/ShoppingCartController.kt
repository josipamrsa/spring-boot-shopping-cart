package com.example.shoppingcart.controllers

import com.example.shoppingcart.request.shopping_cart.ShoppingCartRequest
import com.example.shoppingcart.request.shopping_cart.toShoppingCart
import com.example.shoppingcart.request.shopping_cart_item.ShoppingCartItemRequest
import com.example.shoppingcart.response.shopping_cart.toShoppingCartResponse
import com.example.shoppingcart.services.ShoppingCartService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingCartController(
    private val shoppingCartService: ShoppingCartService
) {
    @GetMapping
    fun helloWorld() = ResponseEntity.ok("Hello world")

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
    fun addItemToCart(@PathVariable cartId: String, @RequestBody request: ShoppingCartItemRequest) =
        ResponseEntity.ok(
            shoppingCartService.addItemToCart(cartId, request)
        )

    @PatchMapping("/remove_item/{cartId}")
    fun removeItemFromCart(@PathVariable cartId: String, @RequestBody request: ShoppingCartItemRequest) =
        ResponseEntity.ok(
            shoppingCartService.removeItemFromCart(cartId, request)
        )

    @DeleteMapping
    fun deleteCart(@RequestBody request: ShoppingCartRequest) =
        ResponseEntity.ok(
            shoppingCartService.deleteShoppingCart(request.toShoppingCart())
        )


}