package com.cornixtech.trackingworld.activities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cornixtech.trackingworld.R
import com.cornixtech.trackingworld.utils.Constants
import com.cornixtech.trackingworld.utils.Constants.USER_LATITUDE
import com.cornixtech.trackingworld.utils.Constants.USER_LONGITUDE
import com.cornixtech.trackingworld.utils.UserDetailManager
import com.cornixtech.trackingworld.utils.UserManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.textfield.TextInputLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

/**CREATED BY NAQI HASSAN 3/9/2020**/

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var isMapInitialized = false
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat = 0.0
    private var long = 0.0
    private var userCurrentLat = 0.0
    private var userCurrentLong = 0.0

    private lateinit var mapFragment: SupportMapFragment
    var userAddMarker: Marker? = null
    var initLocationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        getPermission(permissionListener)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        addLocationFab.shrink()
        addLocationFab.hide()
        setListeners()
//        setButtonGravity(mapFragment.view!!)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapInitialized = true;
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())

        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json
            )
        )
//        mMap.setOnMarkerClickListener {
//            mMap?.setPadding(0, 0, 0, 20)
//            return@setOnMarkerClickListener true
//        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Place Holder"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        getCoordinates {
            if (it) {
                mMap.clear()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, long), 14f))
                initLocationMarker = mMap.addMarker(
                    (MarkerOptions().position(LatLng(lat, long)).title("User Current Location"))
                )
                updateMapWithNewMarkers()
                addLocationFab.show()
            }
        }
        mMap.setOnMapClickListener {
            if (initLocationMarker != null) {
                initLocationMarker!!.remove()
            }
            if (userAddMarker != null) {
                userAddMarker!!.remove()
            }
//            mMap.clear()
            try {
                val address =
                    geocoder.getFromLocation(it.latitude, it.longitude, 1)[0]
                userAddMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(it)
                        .title(address.getCountryName())
                        .snippet(address.getAddressLine(0))
                )
                lat = it.latitude
                long = it.longitude

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                addLocationFab.show()
                addLocationFab.extend()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun updateMapWithNewMarkers() {
        val list =
            UserDetailManager.instance.getLocationsNearYou(LatLng(userCurrentLat, userCurrentLong))
        for (item in list) {
            mMap.addMarker(
                (MarkerOptions().position(LatLng(item.userLat, item.userLong))
                    .title(item.userLocType).snippet(item.userComment))
            ).setIcon(
                BitmapDescriptorFactory.fromResource(R.drawable.twotone_person_pin_circle_white_36)
            )
        }
        Log.e("TrackingWorld", list.toString())
        Log.e("TrackingWorld", "ALL DATABASE "+UserDetailManager.instance.getAllObjectsFromDatabase().toString())

    }

    private fun setButtonGravity(view: View) {
        val locationButton =
            (view.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(
                Integer.parseInt("2")
            )
        val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 30);
    }

    private val permissionListener = object : PermissionListener {
        override fun onPermissionGranted() {

            if (isMapInitialized) {
                mMap.uiSettings.isMyLocationButtonEnabled = true
                mMap.isMyLocationEnabled = true;

            }
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

            if (isMapInitialized) {
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mMap.isMyLocationEnabled = false;
            }
        }

    }

    private fun getCoordinates(cb: (Boolean) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            lat = it.latitude
            long = it.longitude
            userCurrentLat = it.latitude
            userCurrentLong = it.longitude
            Toast.makeText(
                this@MapsActivity,
                "location is $lat,$long",
                Toast.LENGTH_SHORT
            ).show()
            val locationEditor =
                application.getSharedPreferences(Constants.USER_SETTING, Context.MODE_PRIVATE)
                    .edit()
            locationEditor.putFloat(USER_LATITUDE, lat.toFloat())
            locationEditor.putFloat(USER_LONGITUDE, long.toFloat())
            locationEditor.apply()
            Log.d("LOCATIONTAG", "$lat,$long")
            cb(true)
        }
        fusedLocationClient.lastLocation.addOnFailureListener {
            cb(false)
        }
    }

    private fun setListeners() {
        addLocationFab.setOnClickListener {
            addLocationFab.shrink()
            showDialog()
        }
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_location)
        val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val tvAddLocation = dialog.findViewById<View>(R.id.tvAddLocation)
        val etLocationName = dialog.findViewById<EditText>(R.id.etLocationName)
        val etDescription = dialog.findViewById<EditText>(R.id.etDescription)
        val descriptionInputLayout =
            dialog.findViewById<TextInputLayout>(R.id.descriptionInputLayout)
        val locationTypeInputLayout =
            dialog.findViewById<TextInputLayout>(R.id.locationTypeInputLayout)
        tvCancel.setOnClickListener {
            dialog.cancel()
        }
        tvAddLocation.setOnClickListener {
            if (etLocationName.text.toString().trim()
                    .isNotEmpty() && etDescription.text.toString().trim().isNotEmpty()
            ) {
                descriptionInputLayout.error = null
                locationTypeInputLayout.error = null
                addLocationInDataBase(
                    etLocationName.text.toString(),
                    etDescription.text.toString()
                )
                mMap.clear()
                updateMapWithNewMarkers()
                addLocationFab.extend()
                dialog.dismiss()
            } else {
                descriptionInputLayout.error = "Fields are missing"
                locationTypeInputLayout.error = "Fields are missing"
            }


        }
        dialog.show()
        dialog.setOnCancelListener {
            addLocationFab.extend()
        }
    }
    private fun addLocationInDataBase(locName: String, locDesc: String) {
        val userId = UserManager.instance.getLoggedInUserId()
        UserDetailManager.instance.addObjectInDatabase(
            userId,
            locName,
            lat,
            long,
            locDesc,
            "",
            System.currentTimeMillis()
        )

    }

    private fun getPermission(permissionListener: PermissionListener) {
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("If you reject permission, you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check();
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}
