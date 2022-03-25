package com.erdiansyah.githubusers2.data

import android.util.Log
import androidx.lifecycle.*
import com.erdiansyah.githubusers2.app.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MainViewModel(private val preferences: ThemeSettingPreferences): ViewModel() {
    private val _userList = MutableLiveData<ArrayList<ItemsItem>>()
    val userList: LiveData<ArrayList<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchUser(login: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(login)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getTheme(): LiveData<Boolean> = preferences.getTheme().asLiveData()

    fun saveTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            preferences.saveTheme(isDarkTheme)
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}