package com.jpresti.cocktailtest.mvvm

import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jpresti.cocktailtest.R

abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLoader(): View

    fun showNetworkError() {
        hideLoader()
        Snackbar.make(getLoader(), R.string.error_network, Snackbar.LENGTH_LONG).show()
    }

    fun showDataError() {
        hideLoader()
        Snackbar.make(getLoader(), R.string.error_data, Snackbar.LENGTH_LONG).show()
    }

    fun hideLoader() {
        getLoader().visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

}
