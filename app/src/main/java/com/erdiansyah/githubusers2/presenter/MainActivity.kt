package com.erdiansyah.githubusers2.presenter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erdiansyah.githubusers2.R
import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.data.MainViewModel
import com.erdiansyah.githubusers2.data.ThemeSettingPreferences
import com.erdiansyah.githubusers2.data.ViewModelFactory
import com.erdiansyah.githubusers2.databinding.ActivityMainBinding

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private val viewModel: MainViewModel by viewModels()
    private lateinit var rvUser: RecyclerView
    private val list =  ArrayList<ItemsItem>()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = ThemeSettingPreferences.getInstance(themeDataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(preferences)).get(
            MainViewModel::class.java
        )
        rvUser =  binding.rvUser
        val adapter = UserListAdapter(list)
        rvUser.adapter = adapter
        binding.apply {
            rvUser.setHasFixedSize(true)
            bttnSearch.setOnClickListener {
                searchUser()
                hideKeyboard(this@MainActivity, it)
            }
            edtSearchUser.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUser.layoutManager = LinearLayoutManager(this)
        }

        viewModel.userList.observe(this) { userList->
            if (userList !== null) {
                adapter.setUserList(userList)
                visibleLoading(false)
            }
        }
        viewModel.isLoading.observe(this){
            visibleLoading(it)
        }
        adapter.setOnItemClickListener(object: UserListAdapter.OnItemClickListener{
            override fun onItemClicked(data: ItemsItem) {
                showUserDetail(data)
            }
        })
    }

    private fun visibleLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun searchUser(){
        binding.apply {
            val searchQuery = edtSearchUser.text.toString()
            if (searchQuery.isEmpty()) return
            visibleLoading(true)
            viewModel.searchUser(searchQuery)
        }
    }
    private fun showUserDetail(user: ItemsItem){
        val userDetailIntent = Intent(this@MainActivity, UserPageActivity::class.java)
        userDetailIntent.putExtra(UserPageActivity.EXTRA_USERNAME, user.login)
        startActivity(userDetailIntent)
    }
    fun hideKeyboard(activity: Activity, view: View) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.light_theme -> {
                viewModel.getTheme().observe(this) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    viewModel.saveTheme(false)
                }
            }
            R.id.dark_theme -> {
                viewModel.getTheme().observe(this) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    viewModel.saveTheme(true)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
