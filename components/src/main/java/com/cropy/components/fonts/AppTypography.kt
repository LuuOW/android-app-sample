package com.cropy.components.fonts

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cropy.components.R
import com.cropy.components.colors.AppColor

private val JustSans = FontFamily(
    Font(resId = R.font.just_sans_extra_bold, weight = FontWeight.ExtraBold),
    Font(resId = R.font.just_sans_extra_light, weight = FontWeight.ExtraLight),
    Font(resId = R.font.just_sans_light, weight = FontWeight.Light),
    Font(resId = R.font.just_sans_medium, weight = FontWeight.Medium),
    Font(resId = R.font.just_sans_regular, weight = FontWeight.Normal),
    Font(resId = R.font.just_sans_semi_bold, weight = FontWeight.SemiBold),
    Font(resId = R.font.just_sans_bold, weight = FontWeight.Bold),
)

val AppTypography = Typography()

@Immutable
data class Typography(
    //region title
    val titleXXL: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 54.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    val titleXL: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 54.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    val titleL: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    val titleM: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    val titleS: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    val titleXS: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        color = AppColor.Primary
    ),
    //endregion

    //region body
    val bodyXL: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        color = AppColor.Navy600
    ),
    val bodyL: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        color = AppColor.Navy600
    ),
    val bodyM: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
        color = AppColor.Navy600
    ),
    val bodyS: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
        color = AppColor.Navy600
    ),
    val bodyXS: TextStyle = TextStyle(
        fontFamily = JustSans,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp,
        color = AppColor.Navy600
    ),
    //endregion
)