package com.jpresti.cocktailtest.mvvm.cocktailDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import io.reactivex.disposables.Disposable

class CocktailDetailViewModel(val id: Int) : ViewModel() {

    private var disposable: Disposable? = null
    private val model = CocktailDetailModel()
    private val cocktail: MutableLiveData<Resource<Cocktail>> by lazy {
        MutableLiveData<Resource<Cocktail>>().also {
            fetchCocktailDetail()
        }
    }

    private fun fetchCocktailDetail() {
        disposable = model.getCocktailDetails(id, { cocktailResource -> this.cocktail.value = cocktailResource })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun getCocktail(): LiveData<Resource<Cocktail>> {
        return cocktail;
    }

}
