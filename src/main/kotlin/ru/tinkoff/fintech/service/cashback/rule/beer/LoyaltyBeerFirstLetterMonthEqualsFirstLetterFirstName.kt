package ru.tinkoff.fintech.service.cashback.rule.beer

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_BEER
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule
import ru.tinkoff.fintech.service.cashback.utils.monthWithFirstLetter
import java.time.LocalDate

class LoyaltyBeerFirstLetterMonthEqualsFirstLetterFirstName(val transactionInfo: TransactionInfo) : CashbackRule {
    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_BEER
                && transactionInfo.mccCode == MCC_BEER
                && transactionInfo.firstName[0].equals(monthWithFirstLetter.getValue(LocalDate.now().month.value), true)

    override fun calculateCashbackPercent() = 5.0

}