package com.app.tinkoff.news

import android.content.Context
import com.app.tinkoff.news.network.TinkoffNewsApi
import com.app.tinkoff.news.repo.NetworkNewsRepository
import com.app.tinkoff.news.viewmodel.ViewModelFactory
import org.greenrobot.eventbus.EventBus

class Injection {

    companion object {

        fun provideEventBus(): EventBus = EventBus.getDefault()

        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val api = TinkoffNewsApi.create(context)
            val newsRepository = NetworkNewsRepository(api)
            return ViewModelFactory(provideEventBus(), newsRepository)
        }
    }
}