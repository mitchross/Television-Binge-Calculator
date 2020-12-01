package com.vanillax.televisionbingecalculator.app.tbc
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

import com.google.firebase.analytics.FirebaseAnalytics





/**
 * Created by mitch on 5/27/14.
 */
class TelevisionBingeCalculator : Application() {


    private var mFirebaseAnalytics: FirebaseAnalytics? = null


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

//        val fabric = Fabric.Builder(this)
//                .kits(Crashlytics())
//                .build()
//        Fabric.with(fabric)



    }

}
