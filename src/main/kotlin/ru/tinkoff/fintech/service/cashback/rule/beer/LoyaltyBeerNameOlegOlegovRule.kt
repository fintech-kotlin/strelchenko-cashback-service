package ru.tinkoff.fintech.service.cashback.rule.beer

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule

class LoyaltyBeerNameOlegOlegovRule(val transactionInfo: TransactionInfo) : CashbackRule {
    companion object {
        private const val OLEG_FIRST_NAME = "олег"
        private const val OLEGOV_LAST_NAME = "олегов"
    }

    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER
                && transactionInfo.mccCode == MCC_BEER
                && OLEG_FIRST_NAME.equals(transactionInfo.firstName, true)
                && OLEGOV_LAST_NAME.equals(transactionInfo.lastName, true)

    override fun calculateCashbackPercent() = 10.0
}