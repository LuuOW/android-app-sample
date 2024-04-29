package com.lucaskempe.cropy.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.services.models.users
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.ui.screens.items.ButtonData

class UsersViewModel(navController: NavHostController) : ViewModel() {

    val mUsers = MutableLiveData<List<User>>()

    val userQuery = mutableStateOf("")

    val userId = mutableIntStateOf(0)

    // for modal
    val modalButtonState = mutableStateOf(ButtonState.EDIT)

    val fIconList = mutableStateListOf(
        ButtonData(
            id = 0,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com"
        ),

        ButtonData(
            id = 1,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 2,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 3,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 4,
            title = "Buy salad",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 5,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com"
        )
    )

    val sIconList = mutableStateListOf(
        ButtonData(
            id = 0,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com"
        ),

        ButtonData(
            id = 1,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 2,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 3,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 4,
            title = "Buy salad",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 5,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com"
        ),
        ButtonData(
            id = 6,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com"
        )
    )

    val gIconList = mutableStateListOf(
        ButtonData(
            id = 6,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com"
        )
    )

    /*    val fButtons = mutableStateListOf<@Composable () -> Unit>().apply {
            repeat(1) {
                add {
                    CustomButton(navController = navController, vm = this@UsersViewModel)
                }
            }
        }


        val gButtons = mutableStateListOf<@Composable () -> Unit>().apply {
            repeat(1) {
                add {
                    CustomButton(navController = navController, vm = this@UsersViewModel)
                }
            }
        }

        val sButtons = mutableStateListOf<@Composable () -> Unit>().apply {
            repeat(2) {
                add {
                    CustomButton(navController = navController, vm = this@UsersViewModel)
                }
            }
        }*/


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