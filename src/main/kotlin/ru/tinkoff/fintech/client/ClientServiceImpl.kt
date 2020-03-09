package ru.tinkoff.fintech.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.model.Client

@Service
class ClientServiceImpl : ClientService {
    @Value("\${services.url.client}")
    lateinit var url: String

    override fun getClient(id: String): Client {
        val template = RestTemplate()
        return template.getForObject("$url$id", Client::class.java)!!
    }
}