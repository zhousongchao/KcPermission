/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aku.kcpermission

import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.suspendCoroutine

class KcPermissions {

    private lateinit var fragmentManager: FragmentManager

    private val isMarshmallow: Boolean by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    constructor(activity: FragmentActivity) {
        fragmentManager = activity.supportFragmentManager
    }

    constructor(fragment: Fragment) {
        fragmentManager = fragment.childFragmentManager
    }

    private val mKcPermissionsFragment by lazy {
        fragmentManager.findFragmentByTag(TAG) as KcPermissionsFragment?
            ?: KcPermissionsFragment().also {
                fragmentManager
                    .beginTransaction()
                    .add(it, TAG)
                    .commitNow()
            }
    }

    /**
     * Request permissions immediately, **must be invoked during initialization phase
     * of your application**.
     */
    fun request(vararg permissions: String): Deferred<Boolean> {
        return GlobalScope.async(Dispatchers.Main) {
            requestPermissions(*permissions).all { it.granted }
        }
    }

    /**
     * Request permissions immediately, **must be invoked during initialization phase
     * of your application**.
     */
    fun requestEach(vararg permissions: String): Deferred<List<Permission>> {
        return GlobalScope.async(Dispatchers.Main) {
            requestPermissions(*permissions)
        }
    }

    fun requestEachCombined(vararg permissions: String): Deferred<Permission> {
        return GlobalScope.async(Dispatchers.Main) {
            val list = requestPermissions(*permissions)
            Permission(list)
        }
    }

    private suspend fun requestPermissions(vararg permissions: String): List<Permission> {
        val listResult = mutableListOf<Permission>()
        val unrequestedPermissions = mutableListOf<String>()
        permissions.distinct().forEach {
            when {
                isGranted(it) -> listResult.add(Permission(it, true))
                isRevoked(it) -> listResult.add(Permission(it, false))
                else -> unrequestedPermissions.add(it)
            }
        }
        if (unrequestedPermissions.isNotEmpty()) {
            val unrequestedResult = suspendCoroutine<List<Permission>> {
                mKcPermissionsFragment.requestPermissions(it, *unrequestedPermissions.toTypedArray())
            }
            listResult.addAll(unrequestedResult)
        }
        return listResult
    }

    /**
     * Returns true if the permission is already granted.
     *
     *
     * Always true if SDK &lt; 23.
     */
    private fun isGranted(permission: String): Boolean {
        return !isMarshmallow || mKcPermissionsFragment.isGranted(permission)
    }

    /**
     * Returns true if the permission has been revoked by a policy.
     *
     *
     * Always false if SDK &lt; 23.
     */
    private fun isRevoked(permission: String): Boolean {
        return isMarshmallow && mKcPermissionsFragment.isRevoked(permission)
    }

    companion object {

        internal val TAG = KcPermissions::class.java.simpleName
    }

}

