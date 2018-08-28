package com.app.tinkoff.news.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.app.tinkoff.news.repo.NewsRepository
import org.greenrobot.eventbus.EventBus

class ViewModelFactory(private val eventBus: EventBus,
                       private val newsRepository: NewsRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                return MainViewModel(eventBus, newsRepository) as T

            modelClass.isAssignableFrom(NewsContentViewModel::class.java) ->
                return NewsContentViewModel(eventBus, newsRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}