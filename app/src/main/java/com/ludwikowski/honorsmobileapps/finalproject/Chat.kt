package com.ludwikowski.honorsmobileapps.finalproject

data class Chat (val username: String, val iconResourceId: String, val message: String, val timestamp: Long, val likes: Int, val listOfUserLikes: MutableList<String>, val currentUser: String)