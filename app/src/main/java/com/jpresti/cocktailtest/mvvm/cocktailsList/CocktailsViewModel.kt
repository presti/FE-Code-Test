package com.jpresti.cocktailtest.mvvm.cocktailsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import com.jpresti.cocktailtest.util.notifyObservers
import io.reactivex.disposables.Disposable

class CocktailsViewModel : ViewModel() {

    private var disposable: Disposable? = null
    private val model = CocktailsModel()
    private val cocktails: MutableLiveData<Resource<List<Cocktail>>> by lazy {
        MutableLiveData<Resource<List<Cocktail>>>().also {
            fetchCocktails()
        }
    }

    private fun fetchCocktails() {
        disposable = model.getCocktails({ cocktailsRes -> this.cocktails.value = cocktailsRes })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun getCocktails(): LiveData<Resource<List<Cocktail>>> = cocktails

    fun getCocktailDetails(cocktail: Cocktail): Cocktail {
        if (cocktail.instructions == null) {
            model.getCocktailDetails(cocktail.id, { cocktailRes ->
                if (!cocktailRes.isError()) {
                    val fetchedCocktail = cocktailRes.data
                    cocktail.instructions = fetchedCocktail?.instructions
                    cocktail.ingredients = fetchedCocktail?.ingredients
                    cocktail.measures = fetchedCocktail?.measures
                    cocktails.notifyObservers()
                }
            })
        }
        return cocktail
    }
}
