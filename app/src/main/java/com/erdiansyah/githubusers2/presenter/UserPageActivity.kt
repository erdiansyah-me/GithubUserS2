package com.erdiansyah.githubusers2.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.erdiansyah.githubusers2.R
import com.erdiansyah.githubusers2.data.UserData
import com.erdiansyah.githubusers2.data.SharedViewModel
import com.erdiansyah.githubusers2.databinding.ActivityUserPageBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPageBinding
    private val userPageViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val dataBundle = Bundle()
        dataBundle.putString(EXTRA_USERNAME, username)
        binding = ActivityUserPageBinding.inflate(layoutInflater)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = username
        setContentView(binding.root)

        if (username != null) {
            userPageViewModel.setUserData(username)
        }
        userPageViewModel.userData.observe(this){ userData->
            setUserData(userData)
        }

        userPageViewModel.isLoading.observe(this){
            visibleLoading(it)
        }
        tabSections(dataBundle)

        var isFavorit = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = username?.let { userPageViewModel.checkFavorit(it) }?.toInt()
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnFavorit.isChecked = true
                        isFavorit = true
                    } else {
                        binding.btnFavorit.isChecked = false
                        isFavorit = false
                    }
                }
            }
        }

        binding.btnFavorit.setOnClickListener {
            isFavorit = !isFavorit
            if (isFavorit){
                userPageViewModel.userData.value?.let { userData ->
                    userPageViewModel.insertUserFavorit(
                        userData.login, userData.avatarUrl)
                }
                Toast.makeText(applicationContext,"User telah ditambahkan ke list favorit", Toast.LENGTH_SHORT).show()
            } else {
                if (username != null) {
                    userPageViewModel.deleteNonFav(username)
                    Toast.makeText(applicationContext,"User telah dihapus dari list favorit", Toast.LENGTH_SHORT).show()
                }
            }
            binding.btnFavorit.isChecked = isFavorit
        }
    }

    private fun visibleLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {finish()}
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setUserData(userData: UserData){
        val followers = if (userData.followers > 1){
                resources.getString(R.string.follower, userData.followers)
            }else{
                resources.getString(R.string.follower2, userData.followers)
            }

        val repository = if (userData.public_repos > 1){
                resources.getString(R.string.repositories, userData.public_repos)
            }else{
                resources.getString(R.string.repositories2, userData.public_repos)
            }
        val following = resources.getString(R.string.following, userData.following)


        binding.tvUsername.text = userData.login
        binding.tvName.text = userData.name
        Glide.with(this)
            .load(userData.avatarUrl)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .into(binding.avaUserDetail)
        binding.tvFollowers.text = followers
        binding.tvFollowing.text = following
        binding.tvRepository.text = repository
        binding.tvCompany.text = userData.company
        binding.tvLocation.text = userData.location
    }


    fun tabSections(bundle: Bundle){
        val tabPagerAdapter = TabPagerAdapter(this,bundle)

        val viewPager = binding.viewPager2
        val tabs = binding.tabLayout
        viewPager.adapter = tabPagerAdapter
        TabLayoutMediator(tabs,viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.Followers,
            R.string.Following
        )
    }

}