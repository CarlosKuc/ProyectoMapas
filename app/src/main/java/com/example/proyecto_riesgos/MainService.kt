package com.example.proyecto_riesgos


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MainService : AppCompatActivity(), OnMapReadyCallback {

    var rint:Int = 0

    lateinit var mMap: GoogleMap
    lateinit var marker: Marker

    private lateinit var alertDialog: AlertDialog //popup window

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest //Tecnicamente no hace nada, pero la dejo por si las dudas
    val PERMISSION_ID = 1010

    var latitudGlobal: Double = 0.1
    var longitudGlobal: Double = 0.1
    var radioglobal: Double = 0.1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_service)

        rint = intent.getIntExtra("key",0)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    fun firstCustomDialog(){
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.popupwin, null)
        val headertxt = dialogView.findViewById<TextView>(R.id.popuptitle)
        val latitudedit: EditText = dialogView.findViewById(R.id.latitudinput)
        val longitudedit: EditText = dialogView.findViewById(R.id.longitudinput)
        val radioedit: EditText = dialogView.findViewById(R.id.radioinput)
        val currentbtn: Button = dialogView.findViewById(R.id.btncurrent)
        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                var lastLocation: Location = locationResult.lastLocation
                Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
                longitudedit.setText(lastLocation.longitude.toString())
                latitudedit.setText(lastLocation.latitude.toString())
            }
        }
        currentbtn.setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            RequestPermission()
            if(CheckPermission()){
                if(isLocationEnabled()){
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        RequestPermission()
                    }
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                        var location: Location? = task.result
                        if(location == null){
                            var locationRequest =  LocationRequest()
                            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            locationRequest.interval = 0
                            locationRequest.fastestInterval = 0
                            locationRequest.numUpdates = 1
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                            fusedLocationProviderClient!!.requestLocationUpdates(
                                locationRequest,locationCallback, Looper.myLooper()
                            )
                        }else{
                            Toast.makeText(this, "longitud: "+location.longitude+" latitud: "+location.latitude, Toast.LENGTH_SHORT).show()
                            longitudedit.setText(location.longitude.toString())
                            latitudedit.setText(location.latitude.toString())
                        }
                    }
                }else{
                    Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
                }
            }else{
                RequestPermission()
            }
        }
        val aceptarbtn: Button = dialogView.findViewById(R.id.btnaceptar)
        aceptarbtn.setOnClickListener {
            if(latitudedit.text.trim().isNotEmpty() && longitudedit.text.trim().isNotEmpty()
                && radioedit.text.trim().isNotEmpty()){
                val lat = latitudedit.text.toString()
                val lon = longitudedit.text.toString()
                val rad = radioedit.text.toString()

                drawMarker(lat.toDouble(),lon.toDouble(),rad.toDouble())

                alertDialog.cancel()
            }else{
                Toast.makeText(this, "Favor de llenar todos los campos", Toast.LENGTH_SHORT).show()}
        }
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {
            }
        })
        dialogBuilder.setView(dialogView)
        alertDialog = dialogBuilder.create();
        alertDialog.show()
    }

    fun CheckPermission():Boolean{
        //regresa un booleano, true si tenemos permiso, false si no
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }
        return false
    }

    fun RequestPermission(){
        //permite pedirle al usuario que conceda los permisos si se necesita
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //esta funcion regresa el estado del servivicio de locaclizacion
        //si el gps y el internet estan activados regresara true
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    private fun drawCircle(point: LatLng,radius:Double) {
        val circleOptions = CircleOptions()
        circleOptions.center(point)
        circleOptions.radius(radius)
        circleOptions.strokeColor(Color.RED)
        circleOptions.fillColor(0x30ff0000)
        circleOptions.strokeWidth(2f)
        mMap.addCircle(circleOptions)
    }

    fun drawMarker(lat:Double,lon:Double,rad:Double){
        this.longitudGlobal = lon
        this.latitudGlobal = lat
        this.radioglobal = rad
        var ltln = LatLng(latitudGlobal,longitudGlobal)
        marker = mMap.addMarker(MarkerOptions().position(ltln))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ltln))
        drawCircle(ltln,rad)
    }
}