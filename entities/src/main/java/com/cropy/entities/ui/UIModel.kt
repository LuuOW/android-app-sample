package com.cropy.entities.ui

import kotlinx.serialization.Serializable

@Serializable
data class UIModel(
    var model_title: String = "welcome.title",
    var model_description: String = "welcome.description",
    var model_image: String = "",
    val model_buttons_titles: Array<String> = arrayOf("model.button.title_0","model.button.title_1"),
    val model_buttons_images: Array<String> = arrayOf(),
    val model_buttons_actions: Array<String> = arrayOf(),
    var model_primary_color: String = "0xFF2be4dc",
    var model_secondary_color: String = "0xFF243484",
)