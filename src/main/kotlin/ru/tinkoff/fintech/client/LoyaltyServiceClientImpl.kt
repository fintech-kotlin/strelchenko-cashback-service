package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram
import ru.tinkoff.fintech.utils.getForObjectWithEmptyBodyResponseCheck

@Service
class LoyaltyServiceClientImpl(
    private val restTemplate: RestTemplate,
    @Value("\${services.url.loyalty}")
    private val url: String
) : LoyaltyServiceClient {

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {
        return restTemplate.getForObjectWithEmptyBodyResponseCheck("$url$id", LoyaltyProgram::class.java)
    }
}