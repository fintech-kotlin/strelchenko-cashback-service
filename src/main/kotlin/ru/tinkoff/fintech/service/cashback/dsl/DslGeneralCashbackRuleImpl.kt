package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.model.TransactionInfo

class DslGeneralCashbackRuleImpl(
    val condition: (transactionInfo: TransactionInfo) -> Boolean,
    val resultCashbackValue: (transactionInfo: TransactionInfo, previousCashbackValue: Double) -> Double
) : DslGeneralCashbackRule{
    override fun useThisRule(transactionInfo: TransactionInfo): Boolean {
        return condition(transactionInfo)
    }

    override fun calculate(transactionInfo: TransactionInfo, previousCashbackValue: Double): Double {
        return resultCashbackValue(transactionInfo, previousCashbackValue)
    }
}