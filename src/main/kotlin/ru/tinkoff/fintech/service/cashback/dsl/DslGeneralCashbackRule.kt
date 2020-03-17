package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.model.TransactionInfo

interface DslGeneralCashbackRule {
    fun useThisRule(transactionInfo: TransactionInfo): Boolean
    fun calculate(transactionInfo: TransactionInfo, previousCashbackValue: Double): Double
}