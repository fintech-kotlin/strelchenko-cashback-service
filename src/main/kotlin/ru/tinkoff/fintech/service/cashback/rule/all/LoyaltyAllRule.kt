package ru.tinkoff.fintech.service.cashback.rule.all

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.LOYALTY_PROGRAM_ALL
import ru.tinkoff.fintech.service.cashback.MCC_SOFTWARE
import ru.tinkoff.fintech.service.cashback.rule.CashbackRule
import kotlin.math.abs
import kotlin.math.min

class LoyaltyAllRule(val transactionInfo: TransactionInfo) : CashbackRule {
    override fun useThisRule() =
        transactionInfo.loyaltyProgramName == LOYALTY_PROGRAM_ALL
                && transactionInfo.mccCode == MCC_SOFTWARE
                && isPalindromeWithOneReplace((transactionInfo.transactionSum * 100).toInt())

    override fun calculateCashbackPercent(): Double {
        return leastCommonMultiple(transactionInfo.firstName.length, transactionInfo.lastName.length).toDouble() / 1000
    }

    private fun isPalindromeWithOneReplace(value: Int): Boolean {
        var difference = 0
        val chars = value.toString().toCharArray()
        for (i in 0..chars.size / 2) {
            if (chars[i] != chars[chars.size - i - 1]) {
                difference++
            }
        }
        return difference <= 1
    }

    private fun leastCommonMultiple(n: Int, m: Int): Int {
        return abs(n * m) / greatestCommonDivisor(n, m)
    }

    private fun greatestCommonDivisor(n: Int, m: Int): Int {
        return if (n == m) n else greatestCommonDivisor(abs(n - m), min(n, m))
    }
}