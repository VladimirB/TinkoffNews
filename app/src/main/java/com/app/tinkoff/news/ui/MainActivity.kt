package com.app.tinkoff.news.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.app.tinkoff.news.Injection
import com.app.tinkoff.news.R
import com.app.tinkoff.news.databinding.ActivityMainBinding
import com.app.tinkoff.news.event.ErrorEvent
import com.app.tinkoff.news.event.NewsListItemClickEvent
import com.app.tinkoff.news.model.News
import com.app.tinkoff.news.viewmodel.MainViewModel
import org.greenrobot.eventbus.Subscribe

class MainActivity : BaseActivity() {

    companion object {

        private val TAG: String = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private lateinit var newsAdapter: NewsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initializeRecyclerView()

        val viewModelFactory = Injection.provideViewModelFactory(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        val swipeLayout = binding.swipeLayout
        swipeLayout.setOnRefreshListener {
            setAdapterItems(listOf())
            viewModel.getNews()
        }

        viewModel.news.observe(this, Observer { list ->
            list?.let {
                if (swipeLayout.isRefreshing) {
                    swipeLayout.isRefreshing = false
                }
                setAdapterItems(it)
            }
        })
        viewModel.getNews()
    }

    @Subscribe
    fun onError(event: ErrorEvent) {
        val throwable = event.throwable
        Log.e(TAG, throwable.message, throwable)
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
    }

    @Subscribe
    fun onNewsListItemClicked(event: NewsListItemClickEvent) {
        startActivity(Intent(this, NewsContentActivity::class.java).apply {
            putExtra(NewsContentActivity.CONTENT_ID, event.news.id)
        })
    }

    private fun initializeRecyclerView() {
        newsAdapter = NewsRecyclerViewAdapter(eventBus)
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setAdapterItems(items: List<News>) {
        newsAdapter.setNews(items)
        newsAdapter.notifyDataSetChanged()

        binding.recyclerView.scheduleLayoutAnimation()
    }
}
