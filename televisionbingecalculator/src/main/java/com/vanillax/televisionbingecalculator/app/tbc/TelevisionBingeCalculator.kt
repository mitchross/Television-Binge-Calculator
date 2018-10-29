package com.vanillax.televisionbingecalculator.app.tbc

import android.app.Application
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric




/**
 * Created by mitch on 5/27/14.
 */
class TelevisionBingeCalculator : Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null


    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .build()
        Fabric.with(fabric)

        MultiDex.install(this)


    }

}
