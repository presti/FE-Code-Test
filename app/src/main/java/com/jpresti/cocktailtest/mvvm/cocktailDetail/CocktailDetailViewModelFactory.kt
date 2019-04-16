package com.jpresti.cocktailtest.mvvm.cocktailDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CocktailDetailViewModelFactory(val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CocktailDetailViewModel(id) as T
    }
}
