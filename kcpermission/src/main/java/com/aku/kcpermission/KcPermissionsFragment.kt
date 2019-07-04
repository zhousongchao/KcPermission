package com.aku.kcpermission

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

class KcPermissionsFragment : Fragment() {

    private lateinit var mContinuation: Continuation<List<Permission>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun requestPermissions(continuation: Continuation<List<Permission>>, vararg permissions: String) {
        mContinuation = continuation
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        permissions.forEachIndexed { i, s ->
            shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(s)
        }
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    private fun onRequestPermissionsResult(
        permissions: Array<String>,
        grantResults: IntArray,
        shouldShowRequestPermissionRationale: BooleanArray
    ) {

        permissions.mapIndexed { index, permission ->
            Permission(
                permission,
                grantResults[index] == PackageManager.PERMISSION_GRANTED,
                shouldShowRequestPermissionRationale[index]
            )
        }.let {
            mContinuation.resume(it)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isGranted(permission: String): Boolean {
        val fragmentActivity = activity ?: throw IllegalStateException("This fragment must be attached to an activity.")
        return fragmentActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    internal fun isRevoked(permission: String): Boolean {
        val fragmentActivity = activity ?: throw IllegalStateException("This fragment must be attached to an activity.")
        return fragmentActivity.packageManager.isPermissionRevokedByPolicy(permission, activity!!.packageName)
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 42
    }

}
