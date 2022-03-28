package com.erdiansyah.githubusers2.presenter

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erdiansyah.githubusers2.R
import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.data.SharedViewModel
import com.erdiansyah.githubusers2.data.db.FavoritUser
import com.erdiansyah.githubusers2.databinding.ActivityFavoritBinding

class FavoritActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritBinding
    private lateinit var rvUser: RecyclerView
    private val viewModel : SharedViewModel by viewModels()
    private val list =  ArrayList<ItemsItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.favorit_page)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvUser= binding.rvUserListFav
        val adapter = UserListAdapter(list)
        rvUser.adapter = adapter

        viewModel.isLoading.observe(this){
            visibleLoading(it)
        }

        viewModel.getUser().observe(this){
            if (it !== null) {
                val listFavUser = listToArrayList(it)
                adapter.setUserList(listFavUser)
            }
        }
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUser.layoutManager = GridLayoutManager(this, 2)
        } else {
            rvUser.layoutManager = LinearLayoutManager(this)
        }
        adapter.setOnItemClickListener(object: UserListAdapter.OnItemClickListener{
            override fun onItemClicked(data: ItemsItem) {
                showUserDetail(data)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {finish()}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun visibleLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun listToArrayList(favUser: List<FavoritUser>): ArrayList<ItemsItem> {
        val listFavUser = ArrayList<ItemsItem>()
        for (user in favUser) {
            val convertedUserList = ItemsItem (
                user.login,
                user.avatarUrl
            )
            listFavUser.add(convertedUserList)
        }
        return listFavUser
    }
    private fun showUserDetail(user: ItemsItem){
        val userDetailIntent = Intent(this@FavoritActivity, UserPageActivity::class.java)
        userDetailIntent.putExtra(UserPageActivity.EXTRA_USERNAME, user.login)
        startActivity(userDetailIntent)
    }
}