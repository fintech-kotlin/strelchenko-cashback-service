package ru.tinkoff.fintech.service.cashback.decorator

import ru.tinkoff.fintech.model.TransactionInfo

class Multiple666RuleDecorator(
    private val ruleDecorator: RuleDecorator,
    override val transactionInfo: TransactionInfo
) : RuleDecorator {
    constructor(ruleDecorator: RuleDecorator) : this(ruleDecorator, ruleDecorator.transactionInfo)

    override fun calculateCashback(): Double {
        if (transactionInfo.transactionSum % 666 == 0.0) {
            return ruleDecorator.calculateCashback() + 6.66
        }
        return ruleDecorator.calculateCashback()
    }
}