package com.jpresti.cocktailtest.mvvm.cocktailsList

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jpresti.cocktailtest.R
import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import com.jpresti.cocktailtest.mvvm.cocktailDetail.CocktailDetailActivity
import kotlinx.android.synthetic.main.item_list_content.view.*
import java.util.*

class CocktailsRecyclerViewAdapter(private val activity: AppCompatActivity, private val cocktails: List<Cocktail>) :
    RecyclerView.Adapter<CocktailsRecyclerViewAdapter.ViewHolder>(), Filterable {

    private val onClickListener: View.OnClickListener
    private var filteredList: List<Cocktail> = cocktails

    init {
        onClickListener = View.OnClickListener { v ->
            val id = v.tag as Int
            val intent = Intent(v.context, CocktailDetailActivity::class.java).apply {
                putExtra(CocktailDetailActivity.ARG_COCKTAIL_ID, id)
            }
            v.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredList[position]
        holder.setValues(item.name, item.imageUrl)
        holder.setIngredients(item.ingredients)

        with(holder.itemView) {
            tag = item.id
            setOnClickListener(onClickListener)
        }

        val viewModel = ViewModelProviders.of(activity).get(CocktailsViewModel::class.java)
        viewModel.getCocktails().observe(activity, Observer<Resource<List<Cocktail>>> {
            //We have updated an element in the cocktails list
            holder.setIngredients(item.ingredients)
        })
        viewModel.getCocktailDetails(item)
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence?): FilterResults {
            if (charSequence == null || charSequence.isEmpty()) {
                filteredList = cocktails
            } else {
                val search = charSequence.toString().toLowerCase()
                filteredList = cocktails.filter { cocktail -> cocktail.name?.toLowerCase()?.contains(search) ?: false }
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.cocktailItemTitle
        val imageView: ImageView = view.cocktailItemImage
        val ingredientsView: TextView = view.cocktailItemIngredients
        val ingredientsMoreView: TextView = view.cocktailItemIngredientsMore

        fun setValues(itemName: String?, imageUrl: String?) {
            nameView.text = itemName
            imageUrl?.apply {
                Glide
                    .with(itemView)
                    .load(this)
                    .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
            }
        }

        fun setIngredients(ingredients: List<String>?) {
            var ingredientsMoreText = ""
            var ingredientsText = ""
            if (ingredients != null) {
                val limitIngredients = 3
                val includeMoreField = ingredients.size > limitIngredients
                var rangeLimit = ingredients.lastIndex

                if (includeMoreField) {
                    rangeLimit = 1
                    val itemFormatMore = itemView.resources.getString(R.string.cocktail_list_ingredient_more)
                    val qtyMoreIngredients = ingredients.size - limitIngredients + 1
                    ingredientsMoreText = itemFormatMore.format(Locale.ENGLISH, qtyMoreIngredients)
                }

                val sb = StringBuilder()
                val bullet = "\u2022"
                for (i in 0..rangeLimit) {
                    sb.append("$bullet ${ingredients[i]}\n")
                }
                ingredientsText = sb.substring(0, sb.lastIndex)
            }

            ingredientsMoreView.text = ingredientsMoreText
            ingredientsView.text = ingredientsText
        }
    }
}
