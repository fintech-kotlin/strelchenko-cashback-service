package ru.tinkoff.fintech.service.cashback.rule.beer

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule

class LoyaltyBeerNameOlegRule(val transactionInfo: TransactionInfo) : CashbackRule {
    companion object {
        private const val OLEG_FIRST_NAME = "олег"
    }

    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER
                && transactionInfo.mccCode == MCC_BEER
                && OLEG_FIRST_NAME.equals(transactionInfo.firstName, true)

    override fun calculateCashbackPercent() = 7.0
}