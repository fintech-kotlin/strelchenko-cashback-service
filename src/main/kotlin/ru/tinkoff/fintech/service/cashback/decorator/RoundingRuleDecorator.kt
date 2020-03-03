package ru.tinkoff.fintech.service.cashback.decorator

import ru.tinkoff.fintech.model.TransactionInfo

class RoundingRuleDecorator(
    private val ruleDecorator: RuleDecorator,
    override val transactionInfo: TransactionInfo
) : RuleDecorator {
    constructor(ruleDecorator: RuleDecorator) : this(ruleDecorator, ruleDecorator.transactionInfo)

    override fun calculateCashback(): Double {
        return Math.round(ruleDecorator.calculateCashback() * 100.0) / 100.0
    }
}