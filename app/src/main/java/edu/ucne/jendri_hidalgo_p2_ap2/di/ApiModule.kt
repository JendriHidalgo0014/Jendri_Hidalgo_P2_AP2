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
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    const val BASE_URL = "https://gestionhuacalesapi.azurewebsites.net/api/Gastos/"

    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()


    @Provides
    @Singleton
    fun providesRetrofit(moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}