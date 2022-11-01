package com.nowiczenko.andrzej.biblioteka.di

import com.nowiczenko.andrzej.biblioteka.repository.books.BookRepositoryImplementation
import com.nowiczenko.andrzej.biblioteka.repository.books.BooksRepository
import com.nowiczenko.andrzej.biblioteka.repository.users.UserRepository
import com.nowiczenko.andrzej.biblioteka.repository.users.UserRepositoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @ViewModelScoped
    @Binds
    abstract fun bindUserRepository(
        repositoryImplementation: UserRepositoryImplementation
    ): UserRepository

    @ViewModelScoped
    @Binds
    abstract fun bindBookRepository(
        repositoryImplementation: BookRepositoryImplementation
    ): BooksRepository

}