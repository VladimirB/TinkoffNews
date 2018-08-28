package com.app.tinkoff.news.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.app.tinkoff.news.event.ErrorEvent
import com.app.tinkoff.news.model.NewsContent
import com.app.tinkoff.news.network.Response
import com.app.tinkoff.news.repo.NewsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class NewsContentViewModel(private val eventBus: EventBus,
                           private val newsRepository: NewsRepository) : BaseViewModel() {

    val newsContent = MutableLiveData<NewsContent>()

    fun getNewsContent(id: Long) {
        compositeDisposable.add(newsRepository.getNewsContent(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    processResponse(it)
                }, {
                    eventBus.post(ErrorEvent(it))
                }))
    }

    private fun processResponse(response: Response<NewsContent>) {
        when (response.resultCode) {
            "OK" -> {
                newsContent.value = response.payload
            }
            else -> eventBus.post(ErrorEvent(UnknownError()))
        }
    }
}