package com.mindecs.github.di

import com.mindecs.github.BuildConfig
import com.mindecs.github.data.remote.GithubService
import com.mindecs.github.data.repository.GithubRepositoryImpl
import com.mindecs.github.domain.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIMEOUT_SEC = 60L

    //Key names
    private const val USER_AGENT_KEY = "User-Agent"
    private const val ACCEPT_KEY = "Accept"

    //Key values
    private const val USER_AGENT_VALUE = "Android"
    private const val ACCEPT_VALUE = "application/json"


    @Provides
    @Singleton
    fun provideGithubService(): GithubService {
        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(USER_AGENT_KEY, USER_AGENT_VALUE)
                    .header(ACCEPT_KEY, ACCEPT_VALUE)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }

        return Retrofit.Builder()
            .client(okHttpClient.build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubService::class.java)
    }

    @Provides
    @Singleton
    fun provideGithubRepository(api: GithubService): GithubRepository {
        return GithubRepositoryImpl(api)
    }
}