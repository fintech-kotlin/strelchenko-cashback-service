package ru.tinkoff.fintech.exception

class EmptyBodyResponseException(override val message: String): RuntimeException(message)