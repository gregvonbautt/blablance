package dev.gbautin.blablance

import android.app.Application
import dev.gbautin.blablance.data.DataStoreManager

class BlablanceApplication : Application() {
    lateinit var dataStoreManager: DataStoreManager
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        dataStoreManager = DataStoreManager(applicationContext)
    }

    companion object {
        private lateinit var instance: BlablanceApplication

        fun getInstance(): BlablanceApplication = instance
    }
}
