package com.lucaskempe.cropy.di

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistry
import com.lucaskempe.cropy.services.models.User
import com.lucaskempe.cropy.viewmodels.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Provides
    fun provideViewModel():UsersViewModel {
        return UsersViewModel(SavedStateHandle())
    }


}
