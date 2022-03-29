package com.erdiansyah.githubusers2.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.erdiansyah.githubusers2.app.ApiConfig
import com.erdiansyah.githubusers2.data.db.FavoritUser
import com.erdiansyah.githubusers2.data.db.FavoritUserDao
import com.erdiansyah.githubusers2.data.db.FavoritUserRoomDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedViewModel(application: Application): AndroidViewModel(application) {
    private val favoritUserDao: FavoritUserDao
    init {
        val userDb = FavoritUserRoomDb.getInstance(application)
        favoritUserDao = userDb.favoritUserDao()
    }
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    private val _userListFollower = MutableLiveData<ArrayList<ItemsItem>>()
    val userListFollower: LiveData<ArrayList<ItemsItem>> = _userListFollower

    private val _userListFollowing = MutableLiveData<ArrayList<ItemsItem>>()
    val userListFollowing: LiveData<ArrayList<ItemsItem>> = _userListFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUserData(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserData(username)
        client.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _userData.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getUser(): LiveData<List<FavoritUser>> = favoritUserDao.getUser().asLiveData()

    fun insertUserFavorit(login: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoritUser(login,avatarUrl)
            favoritUserDao.insertUser(user)
        }
    }
    suspend fun checkFavorit(login: String) = favoritUserDao.checkUser(login)
    fun deleteNonFav(login: String) {
        CoroutineScope(Dispatchers.IO).launch {
            favoritUserDao.deleteNonFav(login)
        }
    }

    fun setFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowersUser(username)
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(call: Call<ArrayList<ItemsItem>>, response: Response<ArrayList<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userListFollower.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setFollowing(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingUser(username)
        client.enqueue(object : Callback<ArrayList<ItemsItem>> {
            override fun onResponse(call: Call<ArrayList<ItemsItem>>, response: Response<ArrayList<ItemsItem>>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userListFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UserPageViewModel"
    }
}