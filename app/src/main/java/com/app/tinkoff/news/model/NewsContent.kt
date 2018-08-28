package com.app.tinkoff.news.model

data class NewsContent(val title: News,
                       val creationDate: NewsDate,
                       val lastModificationDate: NewsDate,
                       val content: String)