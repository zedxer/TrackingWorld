package com.cornixtech.trackingworld.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**CREATED BY NAQI HASSAN 3/9/2020**/
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

data class PrayerModel(
    val code: Int,
    val `data`: Data,
    val status: String
)

data class Data(
    @SerializedName("1")
    val january: List<DayModel>,
    @SerializedName("2")
    val february: List<DayModel>,
    @SerializedName("3")
    val march: List<DayModel>,
    @SerializedName("4")
    val april: List<DayModel>,
    @SerializedName("5")
    val may: List<DayModel>,
    @SerializedName("6")
    val june: List<DayModel>,
    @SerializedName("7")
    val july: List<DayModel>,
    @SerializedName("8")
    val august: List<DayModel>,
    @SerializedName("9")
    val september: List<DayModel>,
    @SerializedName("10")
    val october: List<DayModel>,
    @SerializedName("11")
    val november: List<DayModel>,
    @SerializedName("12")
    val december: List<DayModel>
)

data class DayModel(
    val date: Date,
    val meta: Meta,
    val timings: Timings
)


data class Date(
    val gregorian: Gregorian,
    val hijri: Hijri,
    val readable: String,
    val timestamp: String
)

data class Meta(
    val latitude: Double,
    val latitudeAdjustmentMethod: String,
    val longitude: Double,
    val method: Method,
    val midnightMode: String,
    val offset: Offset,
    val school: String,
    val timezone: String
)

data class Timings(
    val Asr: String,
    val Dhuhr: String,
    val Fajr: String,
    val Imsak: String,
    val Isha: String,
    val Maghrib: String,
    val Midnight: String,
    val Sunrise: String,
    val Sunset: String
)

data class Gregorian(
    val date: String,
    val day: String,
    val designation: Designation,
    val format: String,
    val month: Month,
    val weekday: Weekday,
    val year: String
)

data class Hijri(
    val date: String,
    val day: String,
    val designation: Designation,
    val format: String,
    val holidays: List<Any>,
    val month: Month,
    val weekday: Weekday,
    val year: String
)

data class Designation(
    val abbreviated: String,
    val expanded: String
)

data class Month(
    val en: String,
    val number: Int
)

data class Weekday(
    val en: String
)


data class Method(
    val id: Int,
    val name: String,
    val params: Params
)

data class Offset(
    val Asr: Int,
    val Dhuhr: Int,
    val Fajr: Int,
    val Imsak: Int,
    val Isha: Int,
    val Maghrib: Int,
    val Midnight: Int,
    val Sunrise: Int,
    val Sunset: Int
)

data class Params(
    val Fajr: Int,
    val Isha: Int
)





