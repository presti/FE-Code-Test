package com.jpresti.cocktailtest.mvvm.cocktailsList

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jpresti.cocktailtest.R
import com.jpresti.cocktailtest.model.Cocktail
import com.jpresti.cocktailtest.model.Resource
import com.jpresti.cocktailtest.mvvm.BaseActivity
import com.jpresti.cocktailtest.mvvm.cocktailDetail.CocktailDetailActivity
import com.jpresti.cocktailtest.util.hideKeyboard
import kotlinx.android.synthetic.main.activity_cocktail_list.*

/**
 * An activity representing a list of Cocktails, which when touched,
 * lead to a [CocktailDetailActivity] representing cocktail details.
 */
class CocktailsActivity : BaseActivity() {

    private var adapter: CocktailsRecyclerViewAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cocktail_list)

        setSupportActionBar(cocktailsToolbar)
        cocktailsToolbar.title = title

        val viewModel = ViewModelProviders.of(this).get(CocktailsViewModel::class.java)
        viewModel.getCocktails().observe(this, Observer<Resource<List<Cocktail>>> { cocktailsResource ->
            if (cocktailsResource.isError()) {
                showNetworkError()
            } else {
                cocktailsResource.data.run {
                    if (this == null) {
                        showDataError()
                    } else {
                        viewModel.getCocktails().removeObservers(this@CocktailsActivity)
                        setupRecyclerView(this)
                    }
                }
            }
        })
    }

    private fun setupRecyclerView(cocktails: List<Cocktail>) {
        hideLoader()
        adapter = CocktailsRecyclerViewAdapter(this, cocktails)
        cocktailsRecycler.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.getActionView() as SearchView
        this.searchView = searchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        // listening to search query text change
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_search -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        // close search view on back button pressed
        val searchView = this.searchView
        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed()
    }

    override fun getLoader(): View = cocktailsLoader

}
