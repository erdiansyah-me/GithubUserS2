package com.erdiansyah.githubusers2.data

import android.util.Log
import androidx.lifecycle.*
import com.erdiansyah.githubusers2.app.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import kotlin.math.log

class MainViewModel(
    private val preferences: ThemeSettingPreferences
) : ViewModel() {


    private val _userList = MutableLiveData<ArrayList<ItemsItem>>()
    val userList: LiveData<ArrayList<ItemsItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _viewState = MutableLiveData(ViewState.default)
    val viewState: LiveData<ViewState> = _viewState

    enum class ViewState {
        empty,
        default,
        avail,
        failure,
        queryEmpty
    }

    fun searchUser(login: String){
        if (login.isEmpty()) {
            _viewState.value = ViewState.default
        }
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUser(login)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                _viewState.value = ViewState.avail
                if (response.isSuccessful) {
                    _userList.value = response.body()?.items
                    if (response.body()?.totalCount == 0) {
                        _viewState.value = ViewState.empty
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _viewState.value = ViewState.queryEmpty
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _viewState.value = ViewState.failure
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