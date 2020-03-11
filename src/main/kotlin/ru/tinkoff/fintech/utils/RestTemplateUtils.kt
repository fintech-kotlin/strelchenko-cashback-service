package ru.tinkoff.fintech.utils

import org.springframework.web.client.RestTemplate
import ru.tinkoff.fintech.exception.EmptyBodyResponseException


// По-хорошему надо бы наследоваться от DefaultResponseErrorHandler и задать его в RestTemplateBuilder'e
// Использую extension функцию из образовательных целей.
fun <T> RestTemplate.getForObjectWithEmptyBodyResponseCheck(
    url: String,
    responseType: Class<T>,
    vararg uriVariables: Any
): T {
    val responseEntity = this.getForEntity(url, responseType, uriVariables)
    if (responseEntity.hasBody()) {
        return responseEntity.body!!
    } else {
        throw EmptyBodyResponseException("Got empty body from this url: $url")
    }
}