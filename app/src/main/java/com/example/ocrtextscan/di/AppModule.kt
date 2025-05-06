package com.example.ocrtextscan.di

import android.app.Application
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.example.ocrtextscan.data.TextRecognitionRepositoryImpl
import com.example.ocrtextscan.domain.TextRecognitionRepository
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application) : Context{
        return application.applicationContext
    }
    @Singleton
    @Provides
    fun provideTextRecognitionRepository(
        context: Context,
        textRecognizer: TextRecognizer,
        clipboardManager: ClipboardManager
    ): TextRecognitionRepository {
       return TextRecognitionRepositoryImpl(
            context = context,
            recognizer = textRecognizer,
            clipboardManager = clipboardManager
        )
    }

    @Singleton
    @Provides
    fun provideRecognizer() : TextRecognizer{
        return TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    @Singleton
    @Provides
    fun provideClipBoardManager(context: Context) : ClipboardManager{
        return context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }
}