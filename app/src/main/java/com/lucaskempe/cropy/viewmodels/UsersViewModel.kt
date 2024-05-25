package com.lucaskempe.cropy.viewmodels

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.saveable
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.ui.screens.items.ButtonData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    stateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : ViewModel() {

    val movedIndex = mutableStateOf(0)

    //val imageUri = MutableLiveData<Uri>()

    var userQuery by stateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    var selectedImageUri by stateHandle.saveable(){
        mutableStateOf<String>("")
    }

    val isImageSelected by derivedStateOf { selectedImageUri.isNotEmpty() }
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
   enabled = mutableStateOf(true)
        ),

        ButtonData(
            id = 2,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(true)
        ),

        ButtonData(
            id = 3,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

        ButtonData(
            id = 4,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),
        ButtonData(
            id = 5,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

        ButtonData(
            id = 6,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

        ButtonData(
            id = 7,
            icon = "https://w7.pngwing.com/pngs/87/485/png-transparent-plus-3d-icon-thumbnail.png",
            title = "Add",
            urlAction = "https://google.com",
   enabled = mutableStateOf(true)
        ),

        ButtonData(
            id = 8,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

        ButtonData(
            id = 1,
            icon = "",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

    )

    var gIconList = mutableStateListOf<ButtonData>(
        ButtonData(
            id = 9,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false),
        ),
        ButtonData(
            id = 10,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false),

        ),
        ButtonData(
            id = 11,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false),
        ),
        ButtonData(
            icon = "https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg",
            id = 12,
            title = "Buy salad",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            id = 13,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            id = 14,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        )
    )

    val sIconList = mutableStateListOf<ButtonData>(
        ButtonData(
            id = 0,
            icon = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png",
            title = "Buy pizza",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),

        ButtonData(
            id = 1,
            icon = "https://clipart-library.com/image_gallery/149336.png",
            title = "Buy burguer",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            id = 2,
            icon = "https://images.vexels.com/media/users/3/230800/isolated/preview/6fae7b492e567aae76ab5220a894087c-cute-sushi-cartoon.png",
            title = "Buy sushi",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            id = 3,
            icon = "https://static.vecteezy.com/system/resources/previews/025/313/067/original/pink-ice-cream-in-bright-cartoon-style-illustration-isolated-on-transparent-background-png.png",
            title = "Buy ice cream",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            icon = "https://png.pngtree.com/png-vector/20190820/ourmid/pngtree-no-image-vector-illustration-isolated-png-image_1694547.jpg",
            id = 4,
            title = "Buy salad",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)
        ),
        ButtonData(
            id = 5,
            icon = "https://png.pngtree.com/png-clipart/20221219/ourmid/pngtree-pasta-or-spaghetti-icon-png-image_6528365.png",
            title = "Buy pasta",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        ),
        ButtonData(
            id = 6,
            icon = "https://i.pinimg.com/originals/2a/af/2a/2aaf2a3014263604586c04f0f6ce1ae7.png",
            title = "Buy cake",
            urlAction = "https://google.com",
   enabled = mutableStateOf(false)

        )
    )
}