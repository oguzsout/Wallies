package com.oguzdogdu.wallieshd.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph

fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
    val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
        }
    }
}

fun Int?.orEmpty(default: Int = 0): Int {
    return this ?: default
}

fun NavController.navigateSafeWithDirection(directions: NavDirections) {
    val destinationId = currentDestination?.getAction(directions.actionId)?.destinationId.orEmpty()
    currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }
        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { navigate(directions) }
        }
    }
}

fun Fragment.setFragmentResultListener(
    requestKey: String,
    listener: (requestKey: String, result: Bundle) -> Unit
) {
    activity?.supportFragmentManager?.setFragmentResultListener(
        requestKey,
        viewLifecycleOwner
    ) { reqKey, result ->
        listener.invoke(reqKey, result)
    }
}
