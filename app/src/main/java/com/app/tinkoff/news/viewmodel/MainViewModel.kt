package com.app.tinkoff.news.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.app.tinkoff.news.event.ErrorEvent
import com.app.tinkoff.news.model.News
import com.app.tinkoff.news.network.Response
import com.app.tinkoff.news.repo.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class MainViewModel(private val eventBus: EventBus,
                    private val newsRepository: NewsRepository) : BaseViewModel() {

    val news = MutableLiveData<List<News>>()

    fun getNews() {
        compositeDisposable.add(newsRepository.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    processResponse(it)
                }, {
                    eventBus.post(ErrorEvent(it))
                }))
    }

    private fun processResponse(response: Response<List<News>>) {
        when (response.resultCode) {
            "OK" -> {
                val sortedList = response.payload.sortedByDescending { selector(it) }
                news.value = sortedList
            }
            else -> {
                eventBus.post(ErrorEvent(UnknownError()))
            }
        }
    }

    private fun selector(news: News) = news.publicationDate.milliseconds
}