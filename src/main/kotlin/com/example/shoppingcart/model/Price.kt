package com.example.shoppingcart.model

import com.example.shoppingcart.model.enums.PriceType
import java.math.BigDecimal

data class Price(
    val type: PriceType,
    val value: BigDecimal,
    val numberOfRecurrences: String?
)
