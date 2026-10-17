package com.example.bakery.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ProductListItem(
    val productId: String,
    val name: String,
    val categoryName: String = "",
    val basePrice: Double = 0.0,
    val avgRating: Double = 0.0,
    val reviewCount: Int = 0,
    val inStock: Boolean = false,
)
@Serializable
data class PagedResult<T>(
    val items: List<T>,
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 0,
)

class CatalogApi(
    private val baseUrl: String,
    private val client: HttpClient = createHttpClient(),
) {
    suspend fun topRated(count: Int = 5): List<ProductListItem> =
        client.get("${baseUrl.trimEnd('/')}/api/products/top-rated") {
            parameter("count", count)
        }.body()

    suspend fun products(pageSize: Int = 5): List<ProductListItem> =
        client.get("${baseUrl.trimEnd('/')}/api/products") {
            parameter("page", 1)
            parameter("pageSize", pageSize)
        }.body<PagedResult<ProductListItem>>().items

    fun close() = client.close()

}

fun createHttpClient(): HttpClient = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(HttpTimeout) {
        connectTimeoutMillis = 5_000
        requestTimeoutMillis = 10_000
    }
}