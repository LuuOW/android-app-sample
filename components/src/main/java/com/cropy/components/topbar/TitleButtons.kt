package com.cropy.components.topbar

import android.media.Image
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cropy.components.colors.AppColor
import com.cropy.components.fonts.AppTypography

@Composable
fun AppTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    leadingIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    trailingIcon: ImageVector? = null,
    onLeadingIconPressed: () -> Unit = {},
    onTrailingIconPressed: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {

        leadingIcon?.let {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onLeadingIconPressed,
            ) {
                Icon(
                    imageVector = it,
                    contentDescription = "back arrow",
                    tint = AppColor.Primary
                )
            }
        }

        title?.let {
            Text(
                text = it,
                style = AppTypography.bodyXL,
                color = AppColor.Primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        trailingIcon?.let {
            IconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = { onTrailingIconPressed.invoke() },
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = it,
                    contentDescription = "trailing icon",
                    tint = AppColor.Primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppTopBarPreview() {
    Surface {
        Column {
            AppTopBar()

            Box(modifier = Modifier.padding(16.dp))

            AppTopBar(
                trailingIcon = Icons.Filled.Place,
            )

            Box(modifier = Modifier.padding(16.dp))

            AppTopBar(
                title = "Trips",
            )

            Box(modifier = Modifier.padding(16.dp))

            AppTopBar(
                title = "Earnings",
                trailingIcon = Icons.AutoMirrored.Filled.List,
            )

            Box(modifier = Modifier.padding(16.dp))

            AppTopBar(
                title = "Select",
                leadingIcon = null,
                trailingIcon = Icons.AutoMirrored.Filled.ExitToApp,
            )
        }

    }
}