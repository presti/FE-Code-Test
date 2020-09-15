package com.jpresti.cocktailtest.mvvm.cocktailsList

import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import com.jpresti.cocktailtest.network.repository.CocktailRepository
import io.reactivex.disposables.Disposable

class CocktailsModel {

    fun getCocktails(onResult: (cocktails: Resource<List<Cocktail>>) -> Unit): Disposable {
        return CocktailRepository.getCocktails(
            { result -> onResult(Resource.success(result.cocktails)) },
            { error -> onResult(Resource.error(error.message))})
    }

    fun getCocktailDetails(id: Int, onResult: (cocktailResource: Resource<Cocktail>) -> Unit): Disposable {
        return CocktailRepository.getCocktailDetail(
            id,
            { result ->
                result.cocktails.apply {
                    val resource = if (isEmpty()) Resource.error("Invalid Id") else Resource.success(get(0))
                    onResult(resource)
                }
            },
            { error -> onResult(Resource.error(error.message)) }
        )
    }
}
