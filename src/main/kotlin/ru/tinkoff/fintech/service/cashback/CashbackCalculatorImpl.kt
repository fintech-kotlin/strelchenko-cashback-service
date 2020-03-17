package ru.tinkoff.fintech.service.cashback

import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.dsl.myRules

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921
internal const val FIRST_NAME_OLEG = "Олег"
internal const val LAST_NAME_OLEGOV = "Олегов"

@Component
class CashbackCalculatorImpl : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return myRules.calculate(transactionInfo)
    }

}