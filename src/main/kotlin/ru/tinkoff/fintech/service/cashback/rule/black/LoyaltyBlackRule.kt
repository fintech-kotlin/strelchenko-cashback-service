package ru.tinkoff.fintech.service.cashback.rule.black

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BLACK
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule

class LoyaltyBlackRule(val transactionInfo: TransactionInfo): CashbackRule {

    override fun useThisRule() = transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BLACK

    override fun calculateCashbackPercent() = 1.0
}