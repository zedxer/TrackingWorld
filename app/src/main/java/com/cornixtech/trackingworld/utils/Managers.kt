package com.cornixtech.trackingworld.utils

import android.location.Location
import com.cornixtech.trackingworld.models.UserDetailModel
import com.cornixtech.trackingworld.models.UserModel
import com.cornixtech.trackingworld.realm.RealmConfigs
import com.google.android.gms.maps.model.LatLng
import io.realm.Realm
import io.realm.RealmObject
import java.util.*
import kotlin.collections.ArrayList

/**CREATED BY NAQI HASSAN 3/9/2020**/

abstract class StorageManager() {

    interface RealmUpdatedCallback {
        fun onUpdated()
    }

    lateinit var realm: Realm

    private var callback: RealmUpdatedCallback? = null

    init {
        realm = RealmConfigs.getDefaultRealm()

    }

    fun setRealmUpdatedCallback(callback: RealmUpdatedCallback) {
        this.callback = callback;
    }

    fun openNewRealmInstance() {
        closeRealm()
        openRealm()
    }

    fun closeRealm() {
        realm.close()
    }

    fun openRealm() {
        realm = RealmConfigs.getDefaultRealm()
    }
}

abstract class DatabaseManagers : StorageManager() {
    abstract fun createObjectInDatabase(id: String): RealmObject?
    abstract fun updateAllObjectsInDatabase(list: List<RealmObject>)
    abstract fun getObjectFromDatabase(id: String): RealmObject?
    abstract fun getAllObjectsFromDatabase(): List<RealmObject>?
    abstract fun removeDataFromDatabase(realm: Realm)
}

class UserManager : DatabaseManagers() {
    var userId: String = ""

    companion object Singleton {
        val instance: UserManager by lazy { UserManager() }
    }

    fun setLoggedInUserId(userIds: String) {
        userId = userIds
    }

    fun getLoggedInUserId() = userId

    override fun createObjectInDatabase(id: String): UserModel? {
        var item = UserModel()
        item.userId = UUID.randomUUID().toString()
//        realm.executeTransaction {
        item = realm.createObject(UserModel::class.java, item.userId)
//        }
        return item
    }

    override fun updateAllObjectsInDatabase(list: List<RealmObject>) {
        realm.executeTransaction {
            var tempUser: UserModel? = null
            for (item in list) {
                if (item is UserModel) {
                    tempUser = realm.where(UserModel::class.java).equalTo("userId", item.userId)
                        .findFirst()
                    if (tempUser != null) {

                        updateProperties(
                            tempUser,
                            item.userName,
                            item.userEmail,
                            item.userPassword,
                            item.userCreationTime
                        )

                    } else {
                        addObjectInDatabase(
                            item.userName,
                            item.userEmail,
                            item.userPassword,
                            item.userCreationTime
                        )
                    }
                }
            }
        }
    }

    override fun getObjectFromDatabase(id: String): RealmObject? {
        return realm.where(UserModel::class.java).equalTo("userId", id).findFirst()
    }

    fun doLoginUser(userEmail: String, userPassword: String): UserModel? {
        return realm.where(UserModel::class.java)
            .equalTo("userEmail", userEmail)
            .equalTo("userPassword", userPassword).findFirst()
    }

    override fun getAllObjectsFromDatabase(): List<RealmObject>? {
        return realm.where(UserModel::class.java).findAll()
    }

    override fun removeDataFromDatabase(realm: Realm) {
        realm.executeTransaction {
            realm.delete(UserModel::class.java)
        }
    }

    fun addObjectInDatabase(
        userName: String,
        userEmail: String, userPassword: String, userCreationTime: Long
    ): String {
        var userItem: UserModel? = null
        realm.executeTransaction {
            userItem = createObjectInDatabase("")
            userItem?.let {
                updateProperties(
                    it,
                    userName,
                    userEmail,
                    userPassword,
                    userCreationTime
                )
            }
        }
        return userItem?.userId ?: ""
    }

    private fun updateProperties(
        userToBeUpdated: UserModel,
        userName: String, userEmail: String, userPassword: String,
        userCreationTime: Long
    ): UserModel {
        userToBeUpdated.userName = userName
        userToBeUpdated.userEmail = userEmail
        userToBeUpdated.userPassword = userPassword
        userToBeUpdated.userCreationTime = userCreationTime

        return userToBeUpdated
    }


}

class UserDetailManager : DatabaseManagers() {

    companion object Singleton {
        val instance: UserDetailManager by lazy { UserDetailManager() }
    }

    override fun createObjectInDatabase(id: String): UserDetailModel? {
        var item = UserDetailModel()
        item.userDetailId = UUID.randomUUID().toString()
//        realm.executeTransaction {
        item = realm.createObject(UserDetailModel::class.java, item.userDetailId)
//        }
        return item
    }

    override fun updateAllObjectsInDatabase(list: List<RealmObject>) {
        realm.executeTransaction {
            var tempUserDetail: UserDetailModel? = null
            for (item in list) {
                if (item is UserDetailModel) {
                    tempUserDetail =
                        realm.where(UserDetailModel::class.java)
                            .equalTo("userDetailId", item.userId)
                            .findFirst()
                    if (tempUserDetail != null) {
                        updateProperties(
                            tempUserDetail,
                            item.userId,
                            item.userLocType,
                            item.userLat,
                            item.userLong,
                            item.userComment,
                            item.userImageUri,
                            item.timeStamp
                        )

                    } else {
                        addObjectInDatabase(
                            item.userId,
                            item.userLocType,
                            item.userLat,
                            item.userLong,
                            item.userComment,
                            item.userImageUri,
                            item.timeStamp
                        )
                    }
                }
            }
        }
    }

    override fun getObjectFromDatabase(id: String): RealmObject? {
        return realm.where(UserDetailModel::class.java).equalTo("userDetailId", id).findFirst()
    }

    override fun getAllObjectsFromDatabase(): List<RealmObject>? {
        return realm.where(UserDetailModel::class.java).findAll()
    }

    override fun removeDataFromDatabase(realm: Realm) {
        realm.executeTransaction {
            realm.delete(UserDetailModel::class.java)
        }
    }

    fun addObjectInDatabase(
        userId: String, userLocType: String, userLat: Double, userLong: Double,
        userComment: String, userImageUri: String, timeStamp: Long
    ) {
        realm.executeTransaction {
            val userItem = createObjectInDatabase("")
            userItem?.let {
                updateProperties(
                    it,
                    userId,
                    userLocType,
                    userLat,
                    userLong,
                    userComment,
                    userImageUri,
                    timeStamp
                )
            }
        }
    }

    private fun updateProperties(
        userDetailToBeUpdated: UserDetailModel, userId: String?, userLocType: String?,
        userLat: Double, userLong: Double, userComment: String, userImageUri: String,
        timeStamp: Long
    ): UserDetailModel {
        userDetailToBeUpdated.userId = userId!!
        userDetailToBeUpdated.userLocType = userLocType!!
        userDetailToBeUpdated.userLat = userLat
        userDetailToBeUpdated.userLong = userLong
        userDetailToBeUpdated.userComment = userComment
        userDetailToBeUpdated.userImageUri = userImageUri
        userDetailToBeUpdated.timeStamp = timeStamp

        return userDetailToBeUpdated
    }

    fun getLocationsNearYou(latLong: LatLng): ArrayList<UserDetailModel> {
        val listOfLocation = realm.where(UserDetailModel::class.java).findAll()
        val nearLocations = ArrayList<UserDetailModel>()
        for (item in listOfLocation) {
            val results = FloatArray(1)
           Location.distanceBetween(latLong.latitude, latLong.longitude, item.userLat, item.userLong, results)
            if (results[0]<=1000){
                nearLocations.add(item)
            }
        }
        return nearLocations
    }

}