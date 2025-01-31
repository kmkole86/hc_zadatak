package com.example.data.api.data_source_impl

import com.example.data.common.runCatchingCancelable
import com.example.data.api.common.ApiConstants.CURSOR_PARAM_KEY
import com.example.data.api.common.ApiConstants.DEFAULT_PAGE_SIZE
import com.example.data.api.common.ApiConstants.LIMIT_PARAM_KEY
import com.example.data.api.common.ApiConstants.LINK_PARAM_KEY
import com.example.data.api.common.ApiConstants.QUERY_PARAM_KEY
import com.example.data.api.model.PlaceDetailsResponse
import com.example.data.api.model.PlacePageResponse
import com.example.data.api.model.PlacesSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.Headers
import kotlinx.coroutines.coroutineScope
import java.net.URL
import javax.inject.Inject

interface PlacesRemoteDataSource {

    suspend fun searchPlace(query: String, pageCursor: String?): Result<PlacePageResponse>

    suspend fun getPlaceDetails(placeId: String): Result<PlaceDetailsResponse>
}

class PlacesRemoteDataSourceImpl @Inject constructor(private val client: HttpClient) :
    PlacesRemoteDataSource {

    override suspend fun searchPlace(
        query: String,
        pageCursor: String?
    ): Result<PlacePageResponse> =
        runCatchingCancelable {
            coroutineScope {
                val result = client.get("search") {
                    url {
                        pageCursor?.let { parameters.append(CURSOR_PARAM_KEY, it) }
                        parameters.append(LIMIT_PARAM_KEY, DEFAULT_PAGE_SIZE.toString())
                        parameters.append(QUERY_PARAM_KEY, query)
                    }
                }

                PlacePageResponse(
                    places = result.body<PlacesSearchResponse>().results,
                    nextPageCursor = result.headers.parseNextPageCursorOrNull()
                )
            }
        }

    override suspend fun getPlaceDetails(placeId: String): Result<PlaceDetailsResponse> =
        runCatchingCancelable {
            coroutineScope {
                client.get(placeId).body<PlaceDetailsResponse>()
            }
        }
}

private fun Headers.parseNextPageCursorOrNull(): String? {
    val value: String? = this[LINK_PARAM_KEY]

    return value?.let {
        //ton of cases not handled, done just for the sake of simplicity
        URL(it.substringAfter("<", "").substringBefore(">")).findParameterValue(CURSOR_PARAM_KEY)
    }
}

private fun URL.findParameterValue(parameterName: String): String? {
    return query.split('&').map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }.firstOrNull { it.first == parameterName }?.second
}