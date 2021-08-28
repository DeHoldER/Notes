package ru.geekbrains.notes

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.jvm.JvmOverloads
import ru.geekbrains.notes.R

class Navigation(private val fragmentManager: FragmentManager, private val resources: Resources) {
    fun addFragment(fragment: Fragment?, containerId: Int, addToBackStack: Boolean) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(containerId, fragment!!)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    @JvmOverloads
    fun addFragment(fragment: Fragment?, addToBackStack: Boolean = true) {
        val isLandscape = resources.getBoolean(R.bool.isLandscape)
        val transaction = fragmentManager.beginTransaction()
        for (i in 0 until fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
        if (!isLandscape) {
            transaction.replace(R.id.fragment_container, fragment!!)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
        } else {
            transaction.replace(R.id.detail_container, fragment!!)
        }
        transaction.commit()
    }
}