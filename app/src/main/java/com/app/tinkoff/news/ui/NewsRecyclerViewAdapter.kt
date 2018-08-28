package com.app.tinkoff.news.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.tinkoff.news.R
import com.app.tinkoff.news.databinding.ListItemNewsBinding
import com.app.tinkoff.news.event.NewsListItemClickEvent
import com.app.tinkoff.news.model.News
import org.greenrobot.eventbus.EventBus

class NewsRecyclerViewAdapter(private val eventBus: EventBus) : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ListItemNewsBinding,
                     private val eventBus: EventBus) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: News) {
            binding.news = news
            binding.root.setOnClickListener {
                eventBus.post(NewsListItemClickEvent(it, news))
            }
        }
    }

    private var news = listOf<News>()

    fun setNews(news: List<News>) {
        this.news = news
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemNewsBinding = DataBindingUtil.inflate(inflater,
                R.layout.list_item_news,
                parent,
                false)
        return ViewHolder(binding, eventBus)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }
}