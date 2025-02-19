# ShoppingCart component

This project is a simple shopping cart microservice component, built with Spring Boot, Kotlin and MongoDB. It features:

- Creating a shopping cart
- Viewing information and contents of a shopping cart
- Adding or removing cart items from the shopping cart
- Clearing the shopping cart
- Purchasing cart items
- Viewing already purchased items
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
executed on it and a `price` field, which is defined by a `Price` complex type. This entity also contains fields that denote when they were created and last modified.

<sup>Note: MongoDB credentials are hardcoded just for application demonstration purposes, as it's not deployed anywhere. If deploying in cloud environment, use environment variables or another equivalent. Never hardcode any sensitive data.</sup>

`Price` type consists of `type`, `value`, and a `numberOfRecurrences` fields, since prices can be both one-time prices or recurring (for example, for subscriptions). The `type` field is defined as a value from
`PriceType` `enum` class, which supports two aforementioned types. There is also an `Action enum` class, which contains the following options:

- `ADD` - when cart item is purchased, this is the action that it will be recorded with
- `MODIFY` - for upgrading any items (for example, upgrading any subscriptions)
- `DELETE` - for example, for cancelling any subscriptions

## Functionalities

As mentioned before, user can create a shopping cart, add or remove items as cart items, view all items currently in the cart or clear all items in it, purchase items, upgrade or cancel existing purchases 
(if those are recurring) and view already purchased items and statistics regarding already purchased items in a time period. 

Shopping carts are saved in a Mongo `shoppingcarts` document collection. Cart items that are added to the shopping cart, but not yet purchased, will just reside within the shopping cart's `cartItems` field. 
Once they are purchased, they will be saved in a different Mongo collection called `cartitems`, with each document containing a link to its original shopping cart. This is done so that items, that can be upgraded
or cancelled, can still be accessible if the user wants to perform one of those actions on the purchased items. In a real-life scenario, upgrades or cancellations are not usually executed through a shopping cart, but since the task was to implement a shopping cart feature with these requirements, a reference to a shopping cart is still contained for already purchased items.

Functionalities are available through `ShoppingCartController`, the first point of contact for any potential clients. An appropriate request and response DTOs are defined for `ShoppingCart` and `CartItem` entities, as well as some utility DTOs such as `TimePeriod` (used for fetching statistics). Detailed description of each functionality is provided via Swagger API, accessible via `localhost:(port)/swagger-ui/index.html` (if accessed locally), where port value will usually be `8080` if not specified differently. Here is a snapshot of available methods from Swagger API: 

![image](https://github.com/user-attachments/assets/e9037ecd-42ad-4ac2-bc8e-94765f7dbd08)

![image](https://github.com/user-attachments/assets/daedc017-95ad-4689-9f81-4d2a5003f3c1)

![image](https://github.com/user-attachments/assets/2c342aee-b7cf-4d7f-808e-6cd173a25b8d)

Functionalities defined in `ShoppingCartController` are implemented by using functions described in `ShoppingCartService` and `CartItemService` (from the `services` package), which are used to communicate with Mongo repositories (`CartItemRepository` and `ShoppingCartRepository`) and database. The repositories have custom functions for finding items by their identifiers, but the `CartItemRepository` also defines a custom function for finding cart items in a certain time period. Since all purchased items are also saved with `purchasedAt` field, a query is defined that fetches all items that have this field set between defined start and end dates, sent as arguments from the function.

