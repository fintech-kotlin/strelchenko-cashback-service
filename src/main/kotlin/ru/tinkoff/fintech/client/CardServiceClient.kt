package ru.tinkoff.fintech.client

import ru.tinkoff.fintech.model.Card

interface CardServiceClient {

    fun getCard(cardNumber: String): Card
}