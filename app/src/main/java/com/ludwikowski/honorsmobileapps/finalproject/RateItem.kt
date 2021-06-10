package com.ludwikowski.honorsmobileapps.finalproject

data class RateItem(val id: String, val userName: String, val petName: String, val petRes: String, val rating: Double, val listOfRaters: MutableList<Rate>)