package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class NotificationServiceClientImpl(
    private val restTemplate: RestTemplate,
    @Value("\${services.url.notification}")
    private val url: String
) : NotificationServiceClient {

    override fun sendNotification(clientId: String, message: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON_UTF8
        val request = HttpEntity(message, headers)

        restTemplate.postForEntity(
            "$url${clientId}/message",
            request,
            String::class.java
        )
    }
}