package com.example.domain.entity.result

sealed class FavouriteStatusResult {
    object FavouriteStatusLoading : FavouriteStatusResult()
    data class FavouriteStatusSuccess(val isFavourite: Boolean) : FavouriteStatusResult()
    data class FavouriteStatusFailed(val error: FavouriteStatusError) :
        FavouriteStatusResult()
}

sealed class FavouriteStatusError {
    object GenericError : FavouriteStatusError()
    data class ApiError(val message: String) : FavouriteStatusError()
}