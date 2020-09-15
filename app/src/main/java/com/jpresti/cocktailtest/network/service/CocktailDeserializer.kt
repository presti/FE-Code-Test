package com.jpresti.cocktailtest.network.service

import android.text.TextUtils
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.jpresti.cocktailtest.model.Cocktail
import java.lang.reflect.Type

object CocktailDeserializer : JsonDeserializer<Cocktail> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Cocktail? {
        val keyId = "idDrink"
        val keyName = "strDrink"
        val keyImageUrl = "strDrinkThumb"
        val keyInstructions = "strInstructions"
        val keyMeasure = "strMeasure"
        val keyIngredient = "strIngredient";

        json?.asJsonObject?.run {
            val id = get(keyId)?.asInt ?: return null
            val name = get(keyName)?.asString
            val imageUrl = get(keyImageUrl)?.asString
            val instructions = get(keyInstructions)?.asString

            // If we don't have instructions, then we are not fetching the details and we
            // don't have ingredients or measures
            var measures: List<String>? = null
            var ingredients: List<String>? = null
            if (has(keyInstructions)) {
                measures = mutableListOf()
                ingredients = mutableListOf()
                var i = 1
                var key = keyIngredient + i
                var keyAdded = true
                while (keyAdded) {
                    keyAdded = false
                    if (keySet().contains(key)) {
                        val ingredient = get(key)
                        if (!ingredient.isJsonNull) {
                            val ingredientString = ingredient.asString
                            if (!TextUtils.isEmpty(ingredientString)) {
                                ingredients.add(ingredientString)
                                measures.add(get(keyMeasure + i).asString)
                                i++
                                key = keyIngredient + i
                                keyAdded = true
                            }
                        }
                    }
                }
            }

            return Cocktail(id, name, imageUrl, instructions, measures, ingredients)
        }
        return null
    }
}
