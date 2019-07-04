package com.aku.kcpermissions.sample

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.aku.kcpermission.request
import com.aku.kcpermission.requestEach
import com.aku.kcpermission.requestEachCombined
import com.aku.kcpermissions.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "KcPermissions"
        const val TAG_RX = "RxPermissions"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        /************   Request   *************/
        btnRequest.setOnClickListener {
            request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            ) {
                Log.d(TAG, it.toString())
                tvPermissionContent.text = "btnRequest:\n".plus(it)
            }

        }
        btnRequestRx.setOnClickListener {
            RxPermissions(this)
                .request(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                )
                .subscribe {
                    Log.d(TAG, it.toString())
                    tvPermissionContent.text = "btnRequestRx:\n".plus(it)
                }
        }

        /************   RequestEach   *************/

        btnRequestEach.setOnClickListener {
            requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            ) {
                var str = "btnRequestEach:\n"
                val permissions = it.joinToString(separator = "\n") { it1 -> it1.toString() }
                tvPermissionContent.text = str.plus(permissions)

            }
        }
        btnRequestEachRx.setOnClickListener {
            tvPermissionContent.text = "btnRequestEachRx:\n"
            RxPermissions(this)
                .requestEach(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                )
                .subscribe {
                    tvPermissionContent.text = tvPermissionContent.text.toString().plus(
                        it.toString()
                    ).plus("\n")
                }
        }
        /************   RequestEach   *************/
        btnRequestEachCombined.setOnClickListener {
            requestEachCombined(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
            ) {
                Log.d(TAG, it.toString())
                tvPermissionContent.text = "btnRequestEachCombined:\n".plus(it)
            }
        }
        btnRequestEachCombinedRx.setOnClickListener {
            RxPermissions(this)
                .requestEachCombined(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
                )
                .subscribe {
                    Log.d(TAG_RX, it.toString())
                    tvPermissionContent.text = "btnRequestEachCombinedRx:\n".plus(it)
                }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
