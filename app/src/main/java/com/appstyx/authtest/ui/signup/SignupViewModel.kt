package com.appstyx.authtest.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.appstyx.authtest.models.LoginRequest
import com.appstyx.authtest.network.ApiService
import com.appstyx.authtest.utils.Resource
import kotlinx.coroutines.Dispatchers

class SignupViewModel(private var apiService: ApiService): ViewModel() {

    fun getGenders() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(apiService.getGenders()))
        } catch (e: java.lang.Exception) {
            Resource.error(data = null, message = e.message ?: "error getting genders")
        }
    }

    fun login(body: LoginRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(apiService.login(body)))
        } catch (e: Exception) {
            Resource.error(data = null, message = e.message ?: "An Error occurred. Please try again")
        }
    }
}

class SignUpViewModelFactory(private var apiService: ApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}