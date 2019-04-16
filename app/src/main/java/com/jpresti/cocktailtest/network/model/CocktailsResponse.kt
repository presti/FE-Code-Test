package com.jpresti.cocktailtest.network.model

import com.google.gson.annotations.SerializedName
import com.jpresti.cocktailtest.model.Cocktail

data class CocktailsResponse(@SerializedName("drinks") val cocktails: List<Cocktail>)
