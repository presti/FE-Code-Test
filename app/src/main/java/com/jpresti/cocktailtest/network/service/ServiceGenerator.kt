package com.jpresti.cocktailtest.network.service

import com.jpresti.cocktailtest.network.api.CocktailsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import com.jpresti.cocktailtest.model.Cocktail

object ServiceGenerator {

    private val retrofit: Retrofit
    val service: CocktailsApiService

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(createGsonConverter())
            .baseUrl("https://www.thecocktaildb.com/api/json/v1/1/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build())
            .build()

        service = retrofit.create(CocktailsApiService::class.java)
    }

    private fun createGsonConverter(): GsonConverterFactory {
        val gson = GsonBuilder()
            .registerTypeAdapter(Cocktail::class.java, CocktailDeserializer)
            .create()
        return GsonConverterFactory.create(gson)
    }
}
