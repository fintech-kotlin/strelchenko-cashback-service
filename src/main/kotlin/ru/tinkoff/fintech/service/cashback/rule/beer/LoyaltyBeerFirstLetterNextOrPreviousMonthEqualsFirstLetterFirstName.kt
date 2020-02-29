package ru.tinkoff.fintech.service.cashback.rule.beer

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule
import ru.tinkoff.fintech.service.cashback.utils.monthWithFirstLetter
import java.time.LocalDate

class LoyaltyBeerFirstLetterNextOrPreviousMonthEqualsFirstLetterFirstName(val transactionInfo: TransactionInfo) : CashbackRule {
    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER
                && transactionInfo.mccCode == MCC_BEER
                && (transactionInfo.firstName[0].equals(firstLetterNextMonth(), true)
                || transactionInfo.firstName[0].equals(firstLetterPreviousMonth(), true))

    private fun firstLetterNextMonth() = monthWithFirstLetter.getValue(LocalDate.now().plusMonths(1).month.value)
    private fun firstLetterPreviousMonth() = monthWithFirstLetter.getValue(LocalDate.now().minusMonths(1).month.value)

    override fun calculateCashbackPercent() = 3.0
}