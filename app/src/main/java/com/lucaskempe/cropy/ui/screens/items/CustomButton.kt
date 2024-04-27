package com.lucaskempe.cropy.ui.screens.items

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.lucaskempe.cropy.ui.screens.ButtonState
import com.lucaskempe.cropy.viewmodels.UsersViewModel

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    vm: UsersViewModel,
    onClick : () -> Unit = {},
) {
    val imageURLForItems = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png"

    Card(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White,),
            shape = AbsoluteRoundedCornerShape(24.dp),
            modifier = modifier.padding(8.dp).size(80.dp)

    ) {

            AsyncImage(model = imageURLForItems,
                contentDescription = "Icon",
                modifier = modifier
                    .clip(CircleShape) )
    }

}

