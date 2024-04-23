package com.lucaskempe.cropy.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.services.models.users

class UsersViewModel: ViewModel() {

    val mUsers = MutableLiveData<List<User>>()

    val userQuery = mutableStateOf("")

    val userId = mutableIntStateOf(0)

    fun fetchUsers() {
        mUsers.postValue(users)
    }

    fun getUserById(id: Int): User {
        return mUsers.value?.find { it.id == id } ?: throw Exception("User not found")
    }

    fun getUser(): User {
        return mUsers.value?.find { it.id == userId.intValue } ?: throw Exception("User not found")
    }


}