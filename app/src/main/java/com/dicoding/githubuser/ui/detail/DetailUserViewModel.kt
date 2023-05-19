package com.dicoding.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubuser.api.ApiConfig
import com.dicoding.githubuser.data.local.FavoriteUser
import com.dicoding.githubuser.data.local.FavoriteUserDao
import com.dicoding.githubuser.data.local.FavoriteUserRoomDatabase
import com.dicoding.githubuser.data.model.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user = MutableLiveData<DetailUserResponse>()
    val isLoading = MutableLiveData<Boolean>()

    private var favoriteUserDao: FavoriteUserDao?
    private var favoriteUserDatabase: FavoriteUserRoomDatabase?

    init {
        favoriteUserDatabase = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserDatabase?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(username: String, id:Int, avatarUrl: String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            favoriteUserDao?.addToFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = favoriteUserDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            favoriteUserDao?.removeFromFavorite(id)
        }
    }

    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}