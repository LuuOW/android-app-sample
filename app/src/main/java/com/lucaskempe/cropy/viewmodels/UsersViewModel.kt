package com.lucaskempe.cropy.viewmodels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.ui.screens.items.CustomButton

class UsersViewModel(navController: NavHostController): ViewModel() {

    val mUsers = MutableLiveData<List<User>>()

    val userQuery = mutableStateOf("")

    val userId = mutableIntStateOf(0)

    // for modal
    val modalButtonState = mutableStateOf(ButtonState.EDIT)

    val fButtons = mutableStateListOf<@Composable () -> Unit>().apply {
        repeat(5) {
            add {
                CustomButton(navController = navController, vm = this@UsersViewModel)
            }
        }
    }

    val gButtons = mutableStateListOf<@Composable () -> Unit>()

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