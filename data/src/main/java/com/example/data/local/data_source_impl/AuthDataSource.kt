package com.example.data.local.data_source_impl

interface AuthDataSource {
    fun getAuthKey(): String
}

object AuthDataSourceImpl : AuthDataSource {
    override fun getAuthKey(): String = "fsq3YmUUOIyJI8dOKxZfTtnlkYSJZ1LIAahConcvUZnKS5I="
}