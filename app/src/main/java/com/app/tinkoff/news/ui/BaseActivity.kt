package com.app.tinkoff.news.ui

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import com.app.tinkoff.news.Injection
import org.greenrobot.eventbus.EventBus

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val eventBus: EventBus = Injection.provideEventBus()

    override fun onStart() {
        super.onStart()
        eventBus.register(this)
    }

    override fun onStop() {
        super.onStop()
        eventBus.unregister(this)
    }
}