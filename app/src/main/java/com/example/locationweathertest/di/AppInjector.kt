package com.example.locationweathertest.di

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.example.cityweather.di.Injectable
import com.example.locationweathertest.App
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection

object AppInjector {

    fun init(app: App) {
        DaggerAppComponent
            .builder()
            .create(app)
            .inject(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

            }

            override fun onActivityStopped(activity: Activity?) {

            }
        })
    }

    private fun handleActivity(activity: Activity?) {
        if (activity is Injectable) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                        super.onFragmentAttached(fm, f, context)
                        if (f is Injectable) {
                            AndroidSupportInjection.inject(f)
                        }
                    }
                }, true)
        }
    }
}