package com.cropy.entities.user


data class User(
    val businessName: String,
    val hours: String,
    val address: String,
    val food: String,
    val image: String = "https://source.android.com/static/docs/setup/images/Android_symbol_green_RGB.png",
    val id: Int,
    ) {

}

val users = listOf(
    User("Jonny's Pizza", "9:00 - 15:00", "lugones 3485", "Pizza", id = 1),
    User("Joane's Burguer", "9:00 - 15:00", "Diag. 73 La Plata", "Pizza", id = 2, image = "https://icons.veryicon.com/png/o/food--drinks/food-7/pizza-23.png"),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Burgers", id = 3),
    // Add entries for 7 more users here following the same format
    User("Empanadas Rec",  "9:00 - 15:00", "lugones 3485", "Sushi", id = 4),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Tacos", id = 5),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Salad", id = 6),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Steak", id = 7),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Chicken", id = 8),
    User("SalchiPapas Co",  "9:00 - 15:00", "lugones 3485", "Seafood", id = 9),
)

