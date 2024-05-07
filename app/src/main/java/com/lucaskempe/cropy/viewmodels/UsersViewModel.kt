package com.lucaskempe.cropy.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.ui.screens.items.ButtonData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    stateHandle: SavedStateHandle
) : ViewModel() {


    val imageUri = MutableLiveData<Uri>()

    var userQuery by stateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var selectedImageUri by stateHandle.saveable(){
        mutableStateOf<String>("")
    }

    fun updateQuery(newQuery: TextFieldValue) {
        userQuery = newQuery
    }

    var currUserId by stateHandle.saveable() {
        mutableIntStateOf(0)
    }
        private set

    fun loadUserProfile(userId: Int) {
        currUserId = userId
    }

    // for modal
    val modalButtonState = mutableStateOf(ButtonState.EDIT)

    val fIconList = mutableStateListOf(
        ButtonData(
            id = 0,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com",
            enabled = true
        ),

        ButtonData(
            id = 7,
            icon = "https://w7.pngwing.com/pngs/87/485/png-transparent-plus-3d-icon-thumbnail.png",
            title = "Add",
            urlAction = "https://google.com",
            enabled = true
        )
    )

    val gIconList = mutableStateListOf<ButtonData>(
        ButtonData(
            id = 1,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com",
            enabled = false
        ),
        ButtonData(
            id = 2,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com",
            enabled = false

        ),
        ButtonData(
            id = 3,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com",
            enabled = false

        ),
        ButtonData(
            id = 4,
            title = "Buy salad",
            urlAction = "https://google.com",
            enabled = false

        ),
        ButtonData(
            id = 5,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com",
            enabled = false

        ),
        ButtonData(
            id = 6,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com",
            enabled = false

        )
    )

    val sIconList = mutableStateListOf<ButtonData>(
        ButtonData(
            id = 0,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com",
            enabled = true
        ),

        ButtonData(
            id = 1,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com",
            enabled = true

        ),
        ButtonData(
            id = 2,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com",
            enabled = true

        ),
        ButtonData(
            id = 3,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com",
            enabled = true

        ),
        ButtonData(
            id = 4,
            title = "Buy salad",
            urlAction = "https://google.com",
            enabled = true
        ),
        ButtonData(
            id = 5,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com",
            enabled = true

        ),
        ButtonData(
            id = 6,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com",
            enabled = true

        )
    )
}