package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.model.TransactionInfo

interface DslCashbackRule {
    fun useThisRule(transactionInfo: TransactionInfo): Boolean
    fun calculateCashbackPercent(transactionInfo: TransactionInfo): Double
}