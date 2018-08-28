package com.app.tinkoff.news.network

import android.content.Context
import com.app.tinkoff.news.NetManager
import com.app.tinkoff.news.model.News
import com.app.tinkoff.news.model.NewsContent
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

interface TinkoffNewsApi {

    companion object {

        private const val BASE_URL = "https://api.tinkoff.ru/v1/"

        private const val CACHE_SIZE: Long = 10 * 1024 * 1024

        fun create(context: Context?): TinkoffNewsApi {
            val okHttpClient = createOkHttpClient(context)

            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(TinkoffNewsApi::class.java)
        }

        private fun createOkHttpClient(context: Context?): OkHttpClient {
            val cacheDirectory = File(context?.cacheDir, "responses")
            val cache = Cache(cacheDirectory, CACHE_SIZE)
            return OkHttpClient().newBuilder()
                    .cache(cache)
                    .addNetworkInterceptor(OnlineResponseInterceptor())
                    .addInterceptor(OfflineResponseInterceptor(context))
                    .build()
        }
    }

    @GET("news")
    fun getNews(): Single<Response<List<News>>>

    @GET("news_content")
    fun getNewsContent(@Query("id") id: Long): Observable<Response<NewsContent>>
}

private class OnlineResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var response = chain.proceed(chain.request())
        val maxAge = 60
        response = response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$maxAge")
                .build()
        return response
    }
}

private class OfflineResponseInterceptor(private val context: Context?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        context?.let {
            val netManager = NetManager(it)
            if (netManager.isConnected == false) {
                request = request.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached")
                        .build()
            }
        }
        return chain.proceed(request)
    }
}