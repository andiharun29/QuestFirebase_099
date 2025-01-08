package com.example.firebasepam

import android.app.Application
import com.example.firebasepam.DepencenciesInjection.AppContainer
import com.example.firebasepam.DepencenciesInjection.MahasiswaContainer

class MahasiswaApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}