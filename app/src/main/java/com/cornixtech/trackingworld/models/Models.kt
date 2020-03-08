package com.cornixtech.trackingworld.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserModel : RealmObject() {
    @PrimaryKey
    var userId = ""
    var userName = ""
    var userEmail = ""
    var userPassword = ""
    var userCreationTime: Long = 0
}

open class UserDetailModel : RealmObject() {
    @PrimaryKey
    var userDetailId = ""
    var userId = ""
    var userLocType = ""
    var userLat: Double = 0.0
    var userLong: Double = 0.0
    var userComment = ""
    var userImageUri = ""
    var timeStamp: Long = 0
    override fun toString(): String {
        return "userDetailId : $userDetailId, userId: $userId, userLocType: $userLocType, userLat: $userLat, userLong: $userLong, userComment: $userComment, userImageUri: $userImageUri, timeStamp: $timeStamp"
    }
}