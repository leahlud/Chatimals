package com.ludwikowski.honorsmobileapps.finalproject

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class AppViewModel : ViewModel() {

    val database = FirebaseDatabase.getInstance()
    val usersReference = database.getReference("users")
    val usernamesReference = database.getReference("usernames")
    val petsReference = database.getReference("pets")
    val chatsReference = database.getReference("chats")
    val ratesReference = database.getReference("rates")

    lateinit var userPet: Pet
    lateinit var tempUser: User
    lateinit var tempChatList: MutableList<Chat>
    lateinit var tempReplylist: MutableList<Reply>
    lateinit var tempRateList: MutableList<RateItem>

    private var _userId = ""
    val userId : String
        get() = _userId
    private var _username = ""
    val username : String
        get() = _username
    private var _messageId = ""
    val messageId : String
        get() = _messageId

    private var _enteredPetName = ""
    val enteredPetName : String
        get() = _enteredPetName
    private var _selectedAnimal = ""
    val selectedAnimal : String
        get() = _selectedAnimal
    private var _selectedFurColor = ""
    val selectedFurColor : String
        get() = _selectedFurColor
    private var _selectedCollarColor = "select"
    val selectedCollarColor : String
        get() = _selectedCollarColor
    private var _chatSortStyle = "newest"
    val chatSortStyle : String
        get() = _chatSortStyle
    private var _standingPetResource = ""
    val standingPetResource : String
        get() = _standingPetResource
    private var _sleepingPetResource = ""
    val sleepingPetResource : String
        get() = _sleepingPetResource
    private var _userToReplyTo = ""
    val userToReplyTo : String
        get() = _userToReplyTo
    private var _replyChatId = ""
    val replyChatId : String
        get() = _replyChatId
    private lateinit var _userToRate : RateItem
    val userToRate: RateItem
        get() = _userToRate


    private var _errorMessage = MutableLiveData("")
    val errorMessage : LiveData<String>
        get() = _errorMessage
    private var _accountCreated = MutableLiveData(false)
    val accountCreated : LiveData<Boolean>
        get() = _accountCreated
    private var _loggedIn = MutableLiveData(false)
    val loggedIn : LiveData<Boolean>
        get() = _loggedIn
    private var _petCreated = MutableLiveData(false)
    val petCreated : LiveData<Boolean>
        get() = _petCreated
    private var _petHunger = MutableLiveData(0)
    val petHunger : LiveData<Int>
        get()= _petHunger
    private var _petMood = MutableLiveData(0)
    val petMood : LiveData<Int>
        get()= _petMood

    private val _chatList = MutableLiveData<MutableList<Chat>>()
    val chatList: LiveData<MutableList<Chat>>
        get() = _chatList
    private val _replyList = MutableLiveData<MutableList<Reply>>()
    val replyList: LiveData<MutableList<Reply>>
        get() = _replyList
    private val _rateList = MutableLiveData<MutableList<RateItem>>()
    val rateList: LiveData<MutableList<RateItem>>
        get() = _rateList
    private val _topThreeList = MutableLiveData<MutableList<RateItem>>()
    val topThreeList: LiveData<MutableList<RateItem>>
        get() = _topThreeList


    /* Sign up */

    fun setErrorBlank(){
        _errorMessage.value = ""
    }
    fun checkUsernameAvailability(username: String, password: String, confirmedPassword: String, termsChecked: Boolean){
        val usernamesQuery: Query = usernamesReference.child(username)
        usernamesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists())
                    usernameNotAvailable()
                else
                    checkInfo(username, password, confirmedPassword, termsChecked)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error!!.message)
            }
        })
    }
    fun checkInfo(usernameFromUser: String, passwordFromUser: String, confirmedPassword: String, termsChecked: Boolean) {
        _errorMessage.value=""
        var noErrors = true

        if(passwordFromUser!=confirmedPassword){
            _errorMessage.value += "Please make sure your passwords match\n"
            noErrors = false
        }
        if(!termsChecked) {
            _errorMessage.value += "Please check the Terms of Use box\n"
            noErrors = false
        }
        if(noErrors){
            makeTempUser(usernameFromUser, passwordFromUser)
            _accountCreated.value = true
        }
    }
    fun checkForSpaces(password: String): Boolean {
        if(password.indexOf(" ")!= -1)
            return false
        return true
    }
    fun setSpaceError(field: String){
        _errorMessage.value = "$field cannot contain spaces"
    }
    fun usernameNotAvailable(){
        _errorMessage.value = "Username already taken\n"
    }
    fun makeTempUser(username: String, password: String){
        tempUser = User(username, password)
    }
    fun createAccount() {
        val newUserRef = usersReference.push()
        newUserRef.setValue(tempUser)
        _userId = newUserRef.key.toString()
        _username = tempUser.username
        usernamesReference.child(username).setValue(_userId)
    }

    /* Log in */

    fun checkLogin(enteredUsername: String, enteredPassword: String){
        _errorMessage.value = ""

        val findUserQuery = database.getReference("users")
        findUserQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val snapshotIterator: Iterable<DataSnapshot> = snapshot.getChildren()
                val iterator = snapshotIterator.iterator()

                var userExists = false
                var passwordMatches = false
                while (iterator.hasNext()) {
                    val currentUser = iterator.next()
                    if (currentUser.child("username").value == enteredUsername) {
                        userExists = true
                        if (currentUser.child("password").value == enteredPassword)
                            passwordMatches = true
                    }
                }
                if (userExists)
                    checkPassword(passwordMatches, enteredUsername)
                else
                    noUser()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error!!.message)
            }
        })
    }
    fun noUser(){
        _errorMessage.value = "User does not exist"
    }
    fun checkPassword(passwordMatches: Boolean, enteredUsername: String){
        if(passwordMatches)
            logInSetup(enteredUsername)
        else
            _errorMessage.value = "Password is incorrect"
    }
    fun logInSetup(username: String){
        _username = username
        val usernameQuery = usernamesReference.child(username)
        usernameQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) _userId = snapshot.value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error!!.message)
            }
        })
        val petQuery = database.getReference("pets/$userId")
        petQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val petRef = snapshot.child(userId)
                userPet = Pet(petRef.child("petName").value.toString(),
                        petRef.child("petResourceString").value.toString(),
                        petRef.child("colorTheme").value.toString(),
                        petRef.child("iconResourceString").value.toString(),
                        petRef.child("mood").value.toString().toInt(),
                        petRef.child("hunger").value.toString().toInt())
                _loggedIn.value = true
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error!!.message)
            }
        })
    }

    /* Create Pet */

    fun setEnteredPetname(petName: String){
        _enteredPetName = petName.capitalize()
    }
    fun setSelectedAnimal(animal: String){
        _selectedAnimal = animal.toLowerCase()
    }
    fun setSelectedFurColor(furColor: String){
        _selectedFurColor = furColor.toLowerCase()
    }
    fun setSelectedCollarColor(collarColor: String){
        _selectedCollarColor = collarColor.toLowerCase()
    }
    fun checkPetCreation(name: String) {
        _petCreated.value = false
        _errorMessage.value = ""
        if(name == "" || name.indexOf(" ") != -1)
            _errorMessage.value = "Pet name cannot be blank or contain spaces\n"
        if(selectedAnimal == "")
            _errorMessage.value += "Please select an animal\n"
        if(selectedFurColor == "")
            _errorMessage.value += "Please select a fur color\n"
        if(selectedCollarColor == "select")
            _errorMessage.value += "Please select a collar color"
        if(errorMessage.value == "")
            _petCreated.value = true
    }
    fun createPet(name: String){
        createAccount()
        val petRes = "${selectedFurColor}_${selectedAnimal}_${selectedCollarColor}_collar"
        val iconRes = "${selectedFurColor}_${selectedAnimal}_${selectedCollarColor}_pfp"
        var colorTheme = ""
        if(selectedCollarColor == "blue")
            colorTheme = "#9FDBFF"
        else if(selectedCollarColor == "green")
            colorTheme = "#9CFFCA"
        else if(selectedCollarColor == "red")
            colorTheme = "#FFA8A7"
        else if(selectedCollarColor == "pink")
            colorTheme = "#FFC0E6"
        else if(selectedCollarColor == "purple")
            colorTheme = "#DACFFD"
        else
            colorTheme = "#FFCBA3"
        userPet = Pet(name.capitalize(), petRes, colorTheme, iconRes, 100, 100)
        var newUserPetRef = petsReference.child(userId)
        newUserPetRef.setValue(userPet)

        val newRate = RateHelperClass(userId, username, name, petRes, 0.0)
        val newRatesRef = ratesReference.child(userId)
        newRatesRef.setValue(newRate)
    }

    /* Chat */

    fun setChatEventListener(){
        chatsReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                getChatList()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                getChatList()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                getChatList()
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.i("Firebase", error!!.message)
            }
        })
    }
    fun getChatList(){
        if(chatSortStyle == "most likes"){
            chatsReference.orderByChild("likes").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tempChatList = mutableListOf()
                    for(childSnapshot in snapshot.children) {
                        val time = childSnapshot.child("timestamp").value.toString().toLong()
                        val message = childSnapshot.child("message").value.toString()
                        val user = childSnapshot.child("user").value.toString()
                        val icon = childSnapshot.child("iconResourceId").value.toString()
                        val likes = childSnapshot.child("likes").value.toString().toInt()

                        val tempList = mutableListOf<String>()
                        if(childSnapshot.child("likesUsersList").exists()){
                            for(index in childSnapshot.child("likesUsersList").children){
                                tempList.add(index.key.toString())
                            }
                        }
                        tempChatList.add(0, Chat(user, icon, message, time, likes, tempList, userId))
                    }
                    _chatList.value = tempChatList
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", error.message)
                }
            })
        }
        else {
            chatsReference.orderByChild("timestamp").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tempChatList = mutableListOf()
                    for(childSnapshot in snapshot.children) {
                        val time = childSnapshot.child("timestamp").value.toString().toLong()
                        val message = childSnapshot.child("message").value.toString()
                        val user = childSnapshot.child("user").value.toString()
                        val icon = childSnapshot.child("iconResourceId").value.toString()
                        val likes = childSnapshot.child("likes").value.toString().toInt()

                        val tempList = mutableListOf<String>()
                        if(childSnapshot.child("likesUsersList").exists()){
                            for(index in childSnapshot.child("likesUsersList").children){
                                tempList.add(index.key.toString())
                            }
                        }
                        if(chatSortStyle == "newest") tempChatList.add(0, Chat(user, icon, message, time, likes, tempList, userId))
                        else tempChatList.add(Chat(user, icon, message, time, likes, tempList, userId))
                    }
                    _chatList.value = tempChatList
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", error.message)
                }
            })
        }
    }
    fun like(chatTimestamp: Long) {
        chatsReference.child("$chatTimestamp").child("likes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentLikes = snapshot.value.toString().toInt()
                chatsReference.child("$chatTimestamp").child("likes").setValue(currentLikes+1)
                chatsReference.child("$chatTimestamp").child("likesUsersList").child(userId).setValue(1)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error.message)
            }
        })
    }
    fun unlike(chatTimestamp: Long){
        chatsReference.child("$chatTimestamp").child("likes").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentLikes = snapshot.value.toString().toInt()
                chatsReference.child("$chatTimestamp").child("likes").setValue(currentLikes-1)
                chatsReference.child("$chatTimestamp").child("likesUsersList").child(userId).removeValue()
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error.message)
            }
        })
    }
    fun addChatToDatabase(message: String, time: Long){
        val newChat = HelperChatClass(username, userPet.iconResourceString, message, time, 0)
        chatsReference.child("$time").setValue(newChat)
    }
    fun setChatSortStyle(style: String){
        _chatSortStyle = style.toLowerCase()
        getChatList()
    }
    fun deleteChat(timeId: Long){
        chatsReference.child("$timeId").removeValue()
    }
    
    /* Reply */

    fun setMessageId(messageId: Long){
        _messageId = messageId.toString()
    }
    fun setReplyEventListener(){
        chatsReference.child("$messageId").child("replies").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                getReplyList()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                getReplyList()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                getReplyList()
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.i("Firebase", error!!.message)
            }
        })
    }
    fun getReplyList(){
        chatsReference.child("$messageId").child("replies").orderByChild("replyTimestamp").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempReplylist = mutableListOf()
                for(childSnapshot in snapshot.children) {
                    val time = childSnapshot.child("replyTimestamp").value.toString().toLong()
                    val message = childSnapshot.child("replyMessage").value.toString()
                    val user = childSnapshot.child("replyUser").value.toString()
                    val pfp = childSnapshot.child("pfp").value.toString()

                    tempReplylist.add(Reply(user, message, time, pfp))
                }
                _replyList.value = tempReplylist
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error!!.message)
            }
        })
    }
    fun addReplyToDatabase(message: String, time: Long){
        val newReply = Reply(username, message, time, userPet.iconResourceString)
        chatsReference.child("$messageId").child("replies").child("$time").setValue(newReply)
    }
    fun clearReplyList(){
        _replyList.value = mutableListOf()
    }
    fun setUserToReplyTo(user: String){
        _userToReplyTo = user
    }
    fun setReplyChatTimeId(id: String){
        _replyChatId = id
    }
    fun deleteReply(timeId: Long){
        chatsReference.child(replyChatId).child("replies").child("$timeId").removeValue()
    }

    /* Pet Fragment */

    fun setMoodCountDownTimer(){
        val timer = object: CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(userId == "") cancel()
            }
            override fun onFinish() {
                if(userId != "") {
                    if (userPet.mood >= 5) userPet.mood -= 5
                    else userPet.mood = 0
                    _petMood.value = userPet.mood
                    updateFirebasePetHealth()
                    cancel()
                    setMoodCountDownTimer()
                }
                cancel()
            }
        }
        timer.start()
    }
    fun setHungerCountDownTimer(){
        val timer = object: CountDownTimer(90000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if(userId == "") cancel()
            }
            override fun onFinish() {
                if(userId != "") {
                    if (userPet.hunger >= 5) userPet.hunger -= 5
                    else userPet.hunger = 0
                    _petHunger.value = userPet.hunger
                    updateFirebasePetHealth()
                    cancel()
                    setHungerCountDownTimer()
                }
                cancel()
            }
        }
        timer.start()
    }
    fun setUpHealthObservers(){
        _petHunger.value = userPet.hunger
        _petMood.value = userPet.mood
    }
    fun addHungerPoints(points: Int){
        if(userPet.hunger + points <= 100) userPet.hunger += points
        else userPet.hunger = 100
        _petHunger.value = userPet.hunger
        updateFirebasePetHealth()
    }
    fun addMoodPoints(points: Int){
        if(userPet.mood + points <= 100) userPet.mood += points
        else userPet.mood = 100
        _petMood.value = userPet.mood
        updateFirebasePetHealth()
    }
    fun updateFirebasePetHealth(){
        if(userId!=""){
            petsReference.child(userId).child("mood").setValue(userPet.mood)
            petsReference.child(userId).child("hunger").setValue(userPet.hunger)
        }
    }
    fun setResources(){
        _standingPetResource = userPet.petResourceString
        if(userPet.petResourceString.indexOf("cat") != -1){
            val petType = userPet.petResourceString.substring(0, userPet.petResourceString.indexOf("cat") + 3)
            _sleepingPetResource = "${petType}_sleeping"
        }
        else {
            val petType = userPet.petResourceString.substring(0, userPet.petResourceString.indexOf("dog") + 3)
            _sleepingPetResource = "${petType}_sleeping"
        }
    }

    /* Rate */

    fun setRateEventListener(){
        ratesReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                getRateList()
            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                getRateList()
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                getRateList()
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.i("Firebase", error!!.message)
            }
        })
    }

    fun getRateList(){
        ratesReference.orderByChild("rating").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tempRateList = mutableListOf()
                for (childSnapshot in snapshot.children) {
                    val uName = childSnapshot.child("userTag").value.toString()
                    val pName = childSnapshot.child("petName").value.toString()
                    val res = childSnapshot.child("petRes").value.toString()
                    val rate = childSnapshot.child("rating").value.toString().toDouble()

                    val tempList = mutableListOf<Rate>()
                    if (childSnapshot.child("ratingsUsersList").exists()) {
                        for (index in childSnapshot.child("ratingsUsersList").children) {
                            tempList.add(Rate(index.key.toString(), index.value.toString().toDouble()))
                        }
                    }
                    tempRateList.add(0, RateItem(childSnapshot.key.toString(), uName, pName, res, rate, tempList))
                }
                getTopThree(tempRateList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error.message)
            }
        })

    }
    fun getTopThree(ratingList: MutableList<RateItem>){
        val topThree= mutableListOf<RateItem>()
        val newRateList = mutableListOf<RateItem>()
        var count = 0
        for(item in ratingList){
            if(item.rating != 0.0 && count < 3){
                topThree.add(item)
                count++
            }
            else newRateList.add(item)
        }
        _topThreeList.value = topThree
        _rateList.value = newRateList
    }

    fun addRateToDatabase(rating: Double){
        val rateId = userToRate.id
        ratesReference.child(rateId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var overallRating = 0.0
                var numberOfRaters = userToRate.listOfRaters.size
                for(rater in snapshot.child("ratingsUsersList").children)
                    overallRating += rater.value.toString().toDouble()

                if(snapshot.child("ratingsUsersList").child(userId).exists()){
                    val currentUserRating = snapshot.child("ratingsUsersList").child(userId).value.toString().toDouble()
                    overallRating = overallRating - currentUserRating
                }
                else numberOfRaters += 1

                val newRating = (overallRating+rating)/(numberOfRaters)
                ratesReference.child(rateId).child("rating").setValue(newRating)
                ratesReference.child(rateId).child("ratingsUsersList").child(userId).setValue(rating)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase", error.message)
            }
        })

    }
    fun setUserToRate(user: RateItem){
        _userToRate = user
    }

    /* General or used by more than one fragment */

    fun setErrorMessage(message: String){
        _errorMessage.value = message
    }
    fun signOut(){
        _userId = ""
        _username = ""
        _messageId = ""
        _enteredPetName = ""
        _selectedAnimal = ""
        _selectedFurColor = ""
        _selectedCollarColor = ""
        _chatSortStyle = "newest"
        _standingPetResource = ""
        _sleepingPetResource = ""
        _errorMessage.value = ""
        _accountCreated.value = false
        _loggedIn.value = false
        _petCreated.value = false
        _petMood.value = 0
        _petHunger.value = 0
    }
}