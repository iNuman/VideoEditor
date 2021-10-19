package com.numan.videoeditor.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.numan.videoeditor.R
import com.numan.videoeditor.utils.Constants.SHARED_PREFERENCES_NAME
import com.numan.videoeditor.utils.helping_methods.HelpingMethodsUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    /*
    * We can pass context to any class that we'll create like this and we can inject them anywhere
    * */
    @Singleton
    @Provides
    fun provideHelpingMethodsUtils(
        @ApplicationContext context: Context
    ) = HelpingMethodsUtils(context)

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.sticker_error)
            .error(R.drawable.sticker_error)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

}