package com.app.tinkoff.news.repo

import com.app.tinkoff.news.model.News
import com.app.tinkoff.news.model.NewsContent
import com.app.tinkoff.news.network.Response
import com.app.tinkoff.news.network.TinkoffNewsApi
import io.reactivex.Observable
import io.reactivex.Single

interface NewsRepository {

    fun getNews(): Single<Response<List<News>>>

    fun getNewsContent(id: Long): Observable<Response<NewsContent>>
}

class NetworkNewsRepository(private val api: TinkoffNewsApi) : NewsRepository {

    override fun getNews() = api.getNews()

    override fun getNewsContent(id: Long) = api.getNewsContent(id)
}