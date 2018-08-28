package com.app.tinkoff.news.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.app.tinkoff.news.Injection
import com.app.tinkoff.news.R
import com.app.tinkoff.news.databinding.ActivityNewsDetailBinding
import com.app.tinkoff.news.event.ErrorEvent
import com.app.tinkoff.news.viewmodel.NewsContentViewModel
import org.greenrobot.eventbus.Subscribe

class NewsContentActivity : BaseActivity() {

    companion object {

        const val CONTENT_ID = "content_id"

        private val TAG: String = NewsContentActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail)

        supportActionBar?.apply {
            title = "News Detail"
            setDisplayHomeAsUpEnabled(true)
        }

        val viewModelFactory = Injection.provideViewModelFactory(this)
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(NewsContentViewModel::class.java)
        viewModel.newsContent.observe(this, Observer {
            binding.setContent(it)
        })
        val contentId = intent.getLongExtra(CONTENT_ID, 0)
        viewModel.getNewsContent(contentId)
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> { super.onOptionsItemSelected(item) }
    }

    @Subscribe
    fun onError(event: ErrorEvent) {
        val throwable = event.throwable
        Log.e(TAG, throwable.message, throwable)
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
    }
}