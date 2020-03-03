package ru.tinkoff.fintech.service.cashback.rule.beer

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule

class LoyaltyBeerSimpleRule(val transactionInfo: TransactionInfo) : CashbackRule {
    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER
                && transactionInfo.mccCode == MCC_BEER

    override fun calculateCashbackPercent() = 2.0
}