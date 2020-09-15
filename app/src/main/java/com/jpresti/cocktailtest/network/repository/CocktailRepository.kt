package com.jpresti.cocktailtest.network.repository

import com.jpresti.cocktailtest.network.service.ServiceGenerator
import com.jpresti.cocktailtest.network.model.CocktailsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object CocktailRepository {

    val serviceG = "Cocktail_glass"

    fun getCocktails(onResult: (result: CocktailsResponse) -> Unit, onError: (error: Throwable) -> Unit): Disposable {
        return ServiceGenerator.service.getCocktails(serviceG)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onResult(result) },
                { error -> onError(error) })
    }

    fun getCocktailDetail(id: Int, onResult: (result: CocktailsResponse) -> Unit, onError: (error: Throwable) -> Unit): Disposable {
        return ServiceGenerator.service.getCocktailDetail(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onResult(result) },
                { error -> onError(error) })
    }

}
