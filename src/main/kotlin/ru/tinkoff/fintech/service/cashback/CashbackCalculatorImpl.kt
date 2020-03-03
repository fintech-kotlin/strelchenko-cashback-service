package ru.tinkoff.fintech.service.cashback

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.rule.all.LoyaltyAllRule
import ru.tinkoff.fintech.service.cashback.rule.black.LoyaltyBlackRule
import ru.tinkoff.fintech.service.cashback.decorator.InitialRuleDecorator
import ru.tinkoff.fintech.service.cashback.decorator.LimitRuleDecorator
import ru.tinkoff.fintech.service.cashback.decorator.Multiple666RuleDecorator
import ru.tinkoff.fintech.service.cashback.decorator.RoundingRuleDecorator
import ru.tinkoff.fintech.service.cashback.rule.beer.*

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

class CashbackCalculatorImpl : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        val rules = listOf(
            LoyaltyBlackRule(transactionInfo),
            LoyaltyAllRule(transactionInfo),
            LoyaltyBeerNameOlegOlegovRule(transactionInfo),
            LoyaltyBeerNameOlegRule(transactionInfo),
            LoyaltyBeerFirstLetterMonthEqualsFirstLetterFirstName(transactionInfo),
            LoyaltyBeerFirstLetterNextOrPreviousMonthEqualsFirstLetterFirstName(transactionInfo),
            LoyaltyBeerSimpleRule(transactionInfo)
        )

        val cashbackPercent = rules.firstOrNull { it.useThisRule() }?.calculateCashbackPercent() ?: 0.0
        val cashbackSum = transactionInfo.transactionSum * cashbackPercent / 100

        return LimitRuleDecorator(
            Multiple666RuleDecorator(
                RoundingRuleDecorator(
                    InitialRuleDecorator(cashbackSum, transactionInfo)
                )
            )
        ).calculateCashback()
    }

}