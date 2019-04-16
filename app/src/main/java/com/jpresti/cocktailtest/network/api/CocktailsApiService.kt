package com.jpresti.cocktailtest.network.api

import com.jpresti.cocktailtest.network.model.CocktailsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailsApiService {

    @GET("filter.php")
    fun getCocktails(@Query("g") g: String): Observable<CocktailsResponse>

    @GET("lookup.php")
    fun getCocktailDetail(@Query("i") id: Int): Observable<CocktailsResponse>
}
