package com.appstyx.authtest.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.appstyx.authtest.network.ApiService
import com.appstyx.authtest.utils.Resource
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private var apiService: ApiService): ViewModel() {

    fun getUserData(token: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(apiService.getUserData(token)))
        } catch (e: Exception) {
            Resource.error(data = null, message = e.message ?: "Error getting user data")
        }
    }
}

class HomeViewModelFactory(private var apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}