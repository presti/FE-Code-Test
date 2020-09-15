package com.jpresti.cocktailtest.mvvm.cocktailDetail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jpresti.cocktailtest.R
import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import com.jpresti.cocktailtest.mvvm.BaseActivity
import kotlinx.android.synthetic.main.activity_cocktail_detail.*

/**
 * An activity representing a single Cocktail detail screen.
 */
class CocktailDetailActivity : BaseActivity() {

    companion object {
        const val ARG_COCKTAIL_ID = "cocktail_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_detail)
        setSupportActionBar(cocktailDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = intent.getIntExtra(ARG_COCKTAIL_ID, -1)
        val viewModelProvider = ViewModelProviders.of(this, CocktailDetailViewModelFactory(id))
        val viewModel = viewModelProvider.get(CocktailDetailViewModel::class.java)
        viewModel.getCocktail().observe(this, Observer<Resource<Cocktail>> { cocktailResource ->
            if (cocktailResource.isError()) {
                showNetworkError()
            } else {
                cocktailResource.data.run {
                    if (this == null) {
                        showDataError()
                    } else {
                        setCocktail(this)
                    }
                }
            }
        })
    }

    fun setCocktail(cocktail: Cocktail) {
        title = cocktail.name

        val sb = StringBuilder()

        cocktail.ingredients?.forEachIndexed { index, ingredient ->
            sb.append("${cocktail.measures?.get(index)?.trim() ?: ""} $ingredient\n")
        }
        sb.deleteCharAt(sb.lastIndex)
        cocktailDetailIngredients.text = sb.toString()

        cocktailDetailInstructions.text = cocktail.instructions
        cocktail.imageUrl.apply {
            Glide
                .with(this@CocktailDetailActivity)
                .load(cocktail.imageUrl)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
                    ): Boolean {
                        showCard()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        showCard()
                        return false
                    }
                })
                .into(cocktailDetailImage)
        }
    }

    override fun getLoader(): View = cocktailDetailLoader

    fun showCard() {
        hideLoader()
        cocktailDetailCard.visibility = View.VISIBLE
    }

}
