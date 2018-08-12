package com.vanillax.televisionbingecalculator.app.TBC

import android.app.Application
import android.support.multidex.MultiDex

import com.google.firebase.analytics.FirebaseAnalytics


/**
 * Created by mitch on 5/27/14.
 */
class TelevisionBingeCalculator : Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null


    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        MultiDex.install(this)


    }

}
