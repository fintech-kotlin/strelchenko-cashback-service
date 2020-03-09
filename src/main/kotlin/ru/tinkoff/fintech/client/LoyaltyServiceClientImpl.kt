package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.LoyaltyProgram

@Service
class LoyaltyServiceClientImpl : LoyaltyServiceClient {
    @Value("\${services.url.loyalty}")
    lateinit var url: String

    override fun getLoyaltyProgram(id: String): LoyaltyProgram {
        val template = RestTemplate()
        return template.getForObject("$url$id", LoyaltyProgram::class.java)!!
    }
}