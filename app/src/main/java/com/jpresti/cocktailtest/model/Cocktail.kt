package com.jpresti.cocktailtest.model

data class Cocktail(
    val id: Int,
    val name: String?,
    val imageUrl: String?,
    var instructions: String?,
    var measures: List<String>?,
    var ingredients: List<String>?
)
