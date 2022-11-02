package com.example.villageplanner2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.villageplanner2.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.getCurrentUser()
        if (user != null) {
            // User is signed in
        } else {
            Toast.makeText(
                this@MapsActivity,
                "You must be logged in to access this page.",
                Toast.LENGTH_LONG
            ).show()
            val mapIntent = Intent(this@MapsActivity, LandingActivity::class.java)
            startActivity(mapIntent)
        }
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        // Add a marker in Sydney and move the camera
        //val sydney = LatLng(-34.0, 151.0)
        val village = LatLng(34.025839586426045, -118.28505473146768)
        mMap.addMarker(MarkerOptions().position(village).title("Marker in USC Village"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(village))
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 18.0f ) )
        //mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    fun NavigateToMapsActivity(view: View?) {
        val mapsIntent = Intent(applicationContext, MapsActivity::class.java)
        startActivity(mapsIntent)
    }

    fun NavigateToRemindersActivity(view: View?) {
        val remindersIntent = Intent(applicationContext, ReminderActivity::class.java)
        startActivity(remindersIntent)
    }

    fun LogOut(view: View?) {
        FirebaseAuth.getInstance().signOut()
        val remindersIntent = Intent(applicationContext, LandingActivity::class.java)
        startActivity(remindersIntent)
    }
}