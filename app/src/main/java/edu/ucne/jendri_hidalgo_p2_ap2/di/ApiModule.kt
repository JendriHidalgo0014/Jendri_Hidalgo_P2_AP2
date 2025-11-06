package edu.ucne.jendri_hidalgo_p2_ap2.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.jendri_hidalgo_p2_ap2.data.remote.ApiService
import edu.ucne.jendri_hidalgo_p2_ap2.data.repository.GastoRepositoryImpl
import edu.ucne.jendri_hidalgo_p2_ap2.domain.repository.GastoRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    private const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/api/Gastos/"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Opcional: define un tiempo de espera para la conexión
            .readTimeout(30, TimeUnit.SECONDS)    // Opcional: define un tiempo de espera para la lectura
            .build()
    }
    @Provides
    @Singleton
    fun providesRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideGastoRepository(
        apiService: ApiService
    ): GastoRepository {
        return GastoRepositoryImpl(apiService)
    }
}