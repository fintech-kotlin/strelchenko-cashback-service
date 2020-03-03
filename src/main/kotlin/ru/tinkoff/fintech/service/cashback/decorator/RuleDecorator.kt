package ru.tinkoff.fintech.service.cashback.decorator

import ru.tinkoff.fintech.model.TransactionInfo

interface RuleDecorator {
    val transactionInfo: TransactionInfo
    fun calculateCashback(): Double
}