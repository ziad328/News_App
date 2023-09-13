package com.example.news.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import com.example.news.databinding.ActivityMainBinding
import com.example.news.ui.home.categories.CategoriesFragment
import com.example.news.ui.home.search.SearchFragment
import com.example.news.ui.home.settings.SettingsFragment
import com.example.news.util.Constants
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.navView.setNavigationItemSelectedListener(this)

        enableHamburgerButton()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                com.example.news.R.id.fragment_container,
                CategoriesFragment()
            ).commit()
        }
    }

    private fun enableHamburgerButton() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            com.example.news.R.string.navigation_drawer_open,
            com.example.news.R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.news.R.menu.main, menu)
        val searchItem = menu.findItem(com.example.news.R.id.action_search)
        val searchView = searchItem.actionView as SearchView?

        searchItem.setOnMenuItemClickListener {
            navigateToSearchFragment()
            true
        }


        // Configure the searchView, such as adding a listener to handle search queries

        // Configure the searchView, such as adding a listener to handle search queries
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle search query submission here
                // Navigate to your Search Fragment and pass the query
                navigateToSearchFragment(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle query text changes here (if needed)
                return true
            }
        })
        return true
    }

    private fun navigateToSearchFragment(query: String? = null) {
        val searchFragment = SearchFragment()
        val bundle = Bundle()
        bundle.putString(Constants.QUERY, query)
        searchFragment.arguments = bundle
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(com.example.news.R.id.fragment_container, searchFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            com.example.news.R.id.nav_categories -> {
                supportFragmentManager.beginTransaction().replace(
                    com.example.news.R.id.fragment_container,
                    CategoriesFragment()
                ).addToBackStack("").commit()

            }

            com.example.news.R.id.nav_settings -> {
                supportFragmentManager.beginTransaction().replace(
                    com.example.news.R.id.fragment_container,
                    SettingsFragment()
                ).addToBackStack("").commit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }
}