package com.example.projekakhir

import android.app.Application
import com.example.projekakhir.ui.AppContainer
import com.example.projekakhir.ui.TerapiContainer

class TerapiApplications : Application(){
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = TerapiContainer()
    }
}