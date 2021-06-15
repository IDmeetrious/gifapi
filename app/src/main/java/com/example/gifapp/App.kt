package com.example.gifapp

import android.app.Application

private const val TAG = "App"
class App: Application() {
    companion object{
        private var instance: App? = null
        fun getInstance(): App{
            if (instance == null) instance = App()
            return instance as App
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}