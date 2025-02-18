# Shopping cart component

This project is a simple shopping cart microservice component, built with Spring Boot, Kotlin and MongoDB. It features:

- Creating a shopping cart
- Viewing information and contents of a shopping cart
- Adding or removing cart items from the shopping cart
- Clearing the shopping cart
- Purchasing cart items
- Modifying purchased items
- Viewing purchase statistics on various offers

## Component structure

Project follows a typical structure, consisting of following packages (modules):

- `config` - Contains configuration classes for various parts of application, in this case it holds certain Mongo configurations
- `controllers` - Contains routes within the application and related logic, in this case we only have one controller that deals with shopping cart and cart item functionalities
- `model` - Contains MongoDB models/schemas
- `repositories` - Contains MongoDB repositories to leverage standard MongoDB functionalities with documents
- `request` - Contains Request DTOs for shopping cart and cart items, as well as related converters 
- `response` - Contains Response DTOs for shopping cart and cart items, as well as related converters
- `services` - Contains functions for working with MongoDB database and Mongo repositories

## Database schema

Since this project uses MongoDB as its database system, database schema consists of `ShoppingCart` and `CartItem` entities. A `ShoppingCart` is defined by its `cartId`, but also contains a field that denotes a list 
of related `CartItem` items, called `cartItems`. A `CartItem` entity consists of `offerId` (which would represent a product from a product catalog in real-life scenario), `relatedCartId`, `action` that has been 
executed on it and a `price` field, which is defined by a `Price` complex type. 

`Price` type consists of `type`, `value`, and a `numberOfRecurrences` fields, since prices can be both one-time prices or recurring (for example, for subscriptions). The `type` field is defined as a value from
`PriceType` `enum` class, which supports two aforementioned types. There is also an `Action enum` class, which contains the following options:

- `ADD` - when cart item is purchased, this is the action that it will be recorded with
- `MODIFY` - for upgrading any items (for example, upgrading any subscriptions)
- `DELETE` - for example, for cancelling any subscriptions

## Functionalities

As mentioned before, user can create a shopping cart, add or remove items as cart items, view all items currently in the cart or clear all items in it, purchase items, upgrade or cancel existing purchases 
(if those are recurring) and view statistics regarding already purchased items in a time period. 

Cart items that are added to the shopping cart, but not yet purchased, will just reside within the shopping cart's cartItems field.
