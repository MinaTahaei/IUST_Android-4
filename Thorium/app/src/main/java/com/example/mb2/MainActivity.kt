package com.example.mb2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.*
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var infoViewModel: CellInfoViewModel
    private var current_location: Location? = null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var latency : Long = 0
    var content_latency :Long = 0
    var jitter = 0
    var jitter_counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = InfoListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val start_sample_btn = findViewById<Button>(R.id.start_sample)


        infoViewModel = ViewModelProvider(this).get(CellInfoViewModel::class.java)
        infoViewModel.allInfos.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        //start sample button listener
        start_sample_btn.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {

                        val mainHandler = Handler(Looper.getMainLooper())

                        mainHandler.post(object : Runnable {
                            @RequiresApi(Build.VERSION_CODES.P)
                            override fun run() {
                                minusOneSecond(tm)
                                mainHandler.postDelayed(this, 5000)
                            }
                        })

                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest?>?,
                        token: PermissionToken?
                    ) {
                    }
                }).check()
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission")
    fun minusOneSecond(tm: TelephonyManager){

        var strength: String = ""
        var gsm_rssi: String = ""
        var lte_rxlev: String = ""
        var umts_rscp: String = ""
        var lte_rsrp: String = ""
        var lte_rsrq: String = ""
        var lte_cqi: String = ""
        var lac: String = ""
        var tac: String = ""
        //var rac: String = ""
        var upSpeed: Int = 0
        var downSpeed: Int = 0

        var plmn: String = ""
        var mcc: String = ""
        var cid : String = ""
        var mnc: String = ""
        var arfcn : String = ""
        var typee: String = ""
        val infos = tm.allCellInfo
        if (infos.size == 0){
            Toast.makeText(this@MainActivity, "No Signal", Toast.LENGTH_SHORT).show()
        }

        try {
            val cellInfo = tm.allCellInfo[0]
            if (cellInfo is CellInfoGsm) {
                val cellSignalStrengthGsm: CellSignalStrengthGsm = cellInfo.cellSignalStrength
                val cellIdentityGsm: CellIdentityGsm = cellInfo.cellIdentity
                lac = cellIdentityGsm.lac.toString()
                mcc = cellIdentityGsm.mccString.toString()
                mnc = cellIdentityGsm.mncString.toString()
                cid = cellIdentityGsm.cid.toString()
                plmn = mcc + mnc
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    arfcn = cellIdentityGsm.arfcn.toString()
                }
                //rac = cellIdentityGsm.rac.toString()
                strength = cellSignalStrengthGsm.dbm.toString()
                gsm_rssi = cellSignalStrengthGsm.asuLevel.toString()
                typee = "GSM"

            }
            if (cellInfo is CellInfoWcdma) {
                val cellSignalStrengthWcdma: CellSignalStrengthWcdma = cellInfo.cellSignalStrength
                val cellIdentityWcdma: CellIdentityWcdma = cellInfo.cellIdentity
                strength = cellSignalStrengthWcdma.dbm.toString()
                lac = cellIdentityWcdma.lac.toString()
                cid = cellIdentityWcdma.cid.toString()
                mcc = cellIdentityWcdma.mccString.toString()
                mnc = cellIdentityWcdma.mncString.toString()
                umts_rscp = cellSignalStrengthWcdma.asuLevel.toString()
                plmn = mcc + mnc
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    arfcn = cellIdentityWcdma.uarfcn.toString()
                }
                //rac
                typee = "UMTS"

            }
            if (cellInfo is CellInfoLte) {
                val cellSignalStrengthLte: CellSignalStrengthLte = cellInfo.cellSignalStrength
                val cellIdentityLte: CellIdentityLte = cellInfo.cellIdentity
                cid = cellIdentityLte.ci.toString()
                mcc = cellIdentityLte.mccString.toString()
                mnc = cellIdentityLte.mncString.toString()
                plmn = mcc + mnc
                tac = cellIdentityLte.tac.toString()
                //rac
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    arfcn = cellIdentityLte.earfcn.toString()
                }
                strength = cellSignalStrengthLte.dbm.toString()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lte_rsrq = cellSignalStrengthLte.rsrq.toString()
                    lte_rsrp = cellSignalStrengthLte.rsrp.toString()
                    lte_cqi = cellSignalStrengthLte.cqi.toString()
                    lte_rxlev = cellSignalStrengthLte.level.toString()
                    cellSignalStrengthLte.rsrp.toString()
                }
                typee = "LTE"

            }
            if (cellInfo is CellInfoCdma) {
                val cellSignalStrengthCdma: CellSignalStrengthCdma = cellInfo.cellSignalStrength
                strength = cellSignalStrengthCdma.dbm.toString()
                umts_rscp = cellSignalStrengthCdma.asuLevel.toString()
                typee = "UMTS"

            }

            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            var netInfo: NetworkInfo? = cm.activeNetworkInfo

            if (netInfo != null && netInfo.isConnected) {
                val nc: NetworkCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
                if (nc != null){
                    downSpeed = nc.getLinkDownstreamBandwidthKbps()
                    upSpeed = nc.getLinkUpstreamBandwidthKbps()
                }
            }

            latency = latencycal("8.8.8.8")
            content_latency = contlatencycal("https://www.aparat.com/")
        }
        catch (e: IndexOutOfBoundsException) {
            Toast.makeText(this@MainActivity, "No Signal", Toast.LENGTH_SHORT).show()
        }
        finally {
            requestNewLocationData()
            if (current_location != null)
            {
                val info = CellInfo(jitter=jitter, upSpeed=upSpeed,downSpeed = downSpeed, gsm_rssi = gsm_rssi, umts_rscp = umts_rscp, lte_rsrq = lte_rsrq, lte_rxlev = lte_rxlev, lte_rsrp = lte_rsrp, lte_cqi = lte_cqi, cid=cid,mcc=mcc,mnc = mnc,plmn=plmn,arfcn = arfcn, latency = latency, content_latency = content_latency, tac = tac, lac = lac, type = typee, strength = strength, longitude = current_location!!.longitude, altitude = current_location!!.latitude, time = System.currentTimeMillis())
                infoViewModel.insert(info)
            }
        }

    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            current_location = mLastLocation
        }
    }

    fun showMap(view: View) {
        println("map button pressed")
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun latencycal(addr: String) :Long {
        val runtime = Runtime.getRuntime()
        var pingg : Long =999999999
        try {
            var a : Long = (System.currentTimeMillis() %100000)
            val IpProcess = runtime.exec("/system/bin/ping -c 1 "+addr)
            val mExitValue  = IpProcess.waitFor(2, TimeUnit.SECONDS)
            if (mExitValue ){
                var b : Long = (System.currentTimeMillis() %100000)
                if(b<=a){
                    pingg = (100000 - a) + b
                }else{
                    pingg = b - a
                }
            }else{
                pingg = 999999999
            }
            if (jitter_counter != 0 && pingg != 999999999.toLong() && latency != 999999999.toLong()){
                var pingg2 = pingg.toInt()
                var pingg3 = kotlin.math.abs((pingg2 - latency).toInt())
                jitter = (((jitter_counter-1)*jitter)+(pingg3)) / jitter_counter
                jitter_counter++
            }else if (jitter_counter == 0){
                jitter_counter++
            }
//            latency = pingg
        } catch (ignore: InterruptedException) {
            ignore.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pingg
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun contlatencycal(addr: String) :Long {
        val runtime = Runtime.getRuntime()
        var pingg : Long =999999999
        try {
            var a : Long = (System.currentTimeMillis() %100000)
            val IpProcess = runtime.exec("/system/bin/ping -c 1 "+addr)
            val mExitValue  = IpProcess.waitFor(2,TimeUnit.SECONDS)
            if (mExitValue ){
                var b : Long = (System.currentTimeMillis() %100000)
                if(b<=a){
                    pingg = (100000 - a) + b
                }else{
                    pingg = b - a
                }
            }else{
                pingg = 999999999
            }
        } catch (ignore: InterruptedException) {
            ignore.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return pingg
    }
}
