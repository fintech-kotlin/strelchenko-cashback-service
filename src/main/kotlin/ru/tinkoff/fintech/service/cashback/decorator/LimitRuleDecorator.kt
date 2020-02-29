package ru.tinkoff.fintech.service.cashback.decorator

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.MAX_CASH_BACK

class LimitRuleDecorator(
    private val ruleDecorator: RuleDecorator,
    override val transactionInfo: TransactionInfo
) : RuleDecorator {
    constructor(ruleDecorator: RuleDecorator) : this(ruleDecorator, ruleDecorator.transactionInfo)

    override fun calculateCashback(): Double {
        val calculatedCashback = ruleDecorator.calculateCashback()
        return when {
            transactionInfo.cashbackTotalValue >= MAX_CASH_BACK -> 0.0
            transactionInfo.cashbackTotalValue + calculatedCashback >= MAX_CASH_BACK -> MAX_CASH_BACK - transactionInfo.cashbackTotalValue
            else -> calculatedCashback
        }
    }
}