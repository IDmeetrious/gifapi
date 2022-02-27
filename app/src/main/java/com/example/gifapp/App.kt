package com.example.gifapp

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object{
        private var instance: App? = null
        fun getInstance(): App{
            if (instance == null) instance = App()
            return instance as App
        }
    }
}