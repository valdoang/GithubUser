package com.dicoding.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubuser.data.local.FavoriteUser
import com.dicoding.githubuser.data.local.FavoriteUserDao
import com.dicoding.githubuser.data.local.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favoriteUserDao: FavoriteUserDao?
    private var favoriteUserDatabase: FavoriteUserRoomDatabase?

    init {
        favoriteUserDatabase = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserDatabase?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favoriteUserDao?.getAllFavoriteUsers()
    }
}