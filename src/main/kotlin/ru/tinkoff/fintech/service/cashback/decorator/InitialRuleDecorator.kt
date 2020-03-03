package ru.tinkoff.fintech.service.cashback.decorator

import ru.tinkoff.fintech.model.TransactionInfo

class InitialRuleDecorator(private val cashbackSum: Double, override val transactionInfo: TransactionInfo) : RuleDecorator {

    override fun calculateCashback(): Double {
        return cashbackSum
    }
}