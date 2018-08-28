package com.app.tinkoff.news.model

data class News(val id: Long,
                val name: String,
                val text: String,
                val publicationDate: NewsDate,
                val bankInfoTypeId: Int)