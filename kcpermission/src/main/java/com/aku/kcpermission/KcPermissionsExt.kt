package com.aku.kcpermission

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kotlinx.coroutines.Deferred


/**
 * @author Zsc
 * @date   2019/7/2
 * @desc
 */
@JvmName("KcPermissions")
fun <T> Deferred<T>.doOnCompleted(action: (T) -> Unit) {
    invokeOnCompletion {
        action(getCompleted())
    }
}

/**********      Fragment      **********/
fun Fragment.request(vararg permissions: String, action: (Boolean) -> Unit) {
    KcPermissions(this)
        .request(*permissions)
        .doOnCompleted { action(it) }
}

fun Fragment.requestEach(vararg permissions: String, action: (List<Permission>) -> Unit) {
    KcPermissions(this)
        .requestEach(*permissions)
        .doOnCompleted { action(it) }
}
fun Fragment.requestEachCombined(vararg permissions: String, action: (Permission) -> Unit) {
    KcPermissions(this)
        .requestEachCombined(*permissions)
        .doOnCompleted { action(it) }
}

/**********      FragmentActivity      **********/
fun FragmentActivity.request(vararg permissions: String, action: (Boolean) -> Unit) {
    KcPermissions(this)
        .request(*permissions)
        .doOnCompleted { action(it) }
}

fun FragmentActivity.requestEach(vararg permissions: String, action: (List<Permission>) -> Unit) {
    KcPermissions(this)
        .requestEach(*permissions)
        .doOnCompleted { action(it) }
}
fun FragmentActivity.requestEachCombined(vararg permissions: String, action: (Permission) -> Unit) {
    KcPermissions(this)
        .requestEachCombined(*permissions)
        .doOnCompleted { action(it) }
}