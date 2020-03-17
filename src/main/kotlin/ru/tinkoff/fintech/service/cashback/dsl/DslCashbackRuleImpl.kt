package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.model.TransactionInfo

class DslCashbackRuleImpl(
    val condition: (transactionInfo: TransactionInfo) -> Boolean,
    val cashBackPercent: (transactionInfo: TransactionInfo) -> Double
) : DslCashbackRule {

    override fun useThisRule(transactionInfo: TransactionInfo): Boolean {
        return condition(transactionInfo)
    }

    override fun calculateCashbackPercent(transactionInfo: TransactionInfo): Double {
        return cashBackPercent(transactionInfo)
    }
}
