package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.utils.monthWithFirstLetter
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToLong

@DslMarker
annotation class RuleContext

class DslRules {
    companion object {
        private val ruleListBuilder = RuleListBuilder()

        fun rules(buildRuleLists: RuleListBuilder.() -> Unit): DslExecutor {
            ruleListBuilder.buildRuleLists()
            return DslExecutor(ruleListBuilder.ruleList, ruleListBuilder.generalRuleList)
        }
    }
}

class DslExecutor(
    private val ruleList: List<DslCashbackRule>,
    private val generalRuleList: List<DslGeneralCashbackRule>
) {
    fun calculate(transactionInfo: TransactionInfo): Double {
        val cashbackPercent =
            ruleList.firstOrNull { it.useThisRule(transactionInfo) }?.calculateCashbackPercent(transactionInfo) ?: 0.0
        var cashbackSum = transactionInfo.transactionSum * cashbackPercent / 100

        generalRuleList
            .asSequence()
            .filter { it.useThisRule(transactionInfo) }
            .forEach { cashbackSum = it.calculate(transactionInfo, cashbackSum) }

        return cashbackSum
    }
}

@RuleContext
class RuleListBuilder {
    val ruleList = mutableListOf<DslCashbackRule>()
    val generalRuleList = mutableListOf<DslGeneralCashbackRule>()

    fun rule(name: String, buildRule: RuleBuilder.() -> DslCashbackRule) {
        val ruleBuilder = RuleBuilder()
        ruleList.add(ruleBuilder.buildRule())
    }

    fun general_rule(name: String, buildGeneralRule: GeneralRuleBuilder.() -> DslGeneralCashbackRule) {
        val ruleBuilder = GeneralRuleBuilder()
        generalRuleList.add(ruleBuilder.buildGeneralRule())
    }
}

@RuleContext
class GeneralRuleBuilder {
    fun all(conditions: AndConditionBuilder.() -> Unit): GeneralConditionResult {
        val conditionsBuilder = AndConditionBuilder()
        conditionsBuilder.conditions()
        return GeneralConditionResult(conditionsBuilder)
    }

    fun then(calculateResult: CashbackValueResult.() -> Unit): DslGeneralCashbackRule {
        val transactionInfoToCashBackResultLambda = { transactionInfo: TransactionInfo, cashbackValue: Double ->
            val cashbackResult = CashbackValueResult(transactionInfo, cashbackValue)
            cashbackResult.calculateResult()
            cashbackResult.cashbackValue
        }
        return DslGeneralCashbackRuleImpl({ true }, transactionInfoToCashBackResultLambda)
    }
}

@RuleContext
class GeneralConditionResult(private val andConditionBuilder: AndConditionBuilder) {
    private val transactionInfoToBooleanLambda: (transactionInfo: TransactionInfo) -> Boolean = {
        val transactionInfo = it
        andConditionBuilder.andConditionList.all { it(transactionInfo) }
    }

    infix fun then(calculateResult: CashbackValueResult.() -> Unit): DslGeneralCashbackRule {
        val transactionInfoToCashBackResultLambda = { transactionInfo: TransactionInfo, cashbackValue: Double ->
            val cashbackResult = CashbackValueResult(transactionInfo, cashbackValue)
            cashbackResult.calculateResult()
            cashbackResult.cashbackValue
        }
        return DslGeneralCashbackRuleImpl(transactionInfoToBooleanLambda, transactionInfoToCashBackResultLambda)
    }
}

class CashbackValueResult(transactionInfo: TransactionInfo, var cashbackValue: Double) {
    private val cashbackTotalValue = transactionInfo.cashbackTotalValue
    fun roundCashback() {
        cashbackValue = (cashbackValue * 100.0).roundToLong() / 100.0
    }

    fun add(add: Double) {
        cashbackValue += add
    }

    fun limit(limitValue: Double) {
        cashbackValue = when {
            cashbackTotalValue >= limitValue -> 0.0
            cashbackTotalValue + cashbackValue >= limitValue -> limitValue - cashbackTotalValue
            else -> cashbackValue
        }
    }
}


@RuleContext
class RuleBuilder {
    fun all(conditions: AndConditionBuilder.() -> Unit): ConditionResult {
        val conditionsBuilder = AndConditionBuilder()
        conditionsBuilder.conditions()
        return ConditionResult(conditionsBuilder)
    }
}

@RuleContext
class ConditionResult(private val andConditionBuilder: AndConditionBuilder) {
    private val transactionInfoToBooleanLambda: (transactionInfo: TransactionInfo) -> Boolean = {
        val transactionInfo = it
        andConditionBuilder.andConditionList.all { it(transactionInfo) }
    }

    infix fun then(calculateCashbackPercent: CashbackPercentResult.() -> Unit): DslCashbackRule {
        val transactionInfoToCashbackPercentLambda: (TransactionInfo) -> Double = {
            val cashbackPercent = CashbackPercentResult(it)
            cashbackPercent.calculateCashbackPercent()
            cashbackPercent.cashbackPercent
        }
        return DslCashbackRuleImpl(transactionInfoToBooleanLambda, transactionInfoToCashbackPercentLambda)
    }
}

@RuleContext
class AndConditionBuilder {
    val andConditionList = mutableListOf<(transactionInfo: TransactionInfo) -> Boolean>()

    fun eval(condition: ConditionBuilder.() -> Boolean) {
        andConditionList.add { ConditionBuilder(it).condition() }
    }
}

@RuleContext
class ConditionBuilder(val transactionInfo: TransactionInfo) {
    val loyaltyProgramName = transactionInfo.loyaltyProgramName
    val cashbackTotalValue = transactionInfo.cashbackTotalValue
    val mccCode = transactionInfo.mccCode
    val firstName = transactionInfo.firstName
    val lastName = transactionInfo.lastName
    val transactionSum = transactionInfo.transactionSum
    val firstLetterFirstName = transactionInfo.firstName[0]
    val firstLetterCurrentMonth = monthWithFirstLetter.getValue(LocalDate.now().month.value)
    val firstLetterPreviousMonth = monthWithFirstLetter.getValue(LocalDate.now().minusMonths(1).month.value)
    val firstLetterNextMonth = monthWithFirstLetter.getValue(LocalDate.now().plusMonths(1).month.value)
    val transactionSumInKopeksIsPalindromeWithOneReplace =
        transactionSumInKopeksIsPalindromeWithOneReplace(transactionInfo)

    private fun transactionSumInKopeksIsPalindromeWithOneReplace(transactionInfo: TransactionInfo): Boolean {
        val transactionSumInKopeks = (transactionInfo.transactionSum * 100).toInt()
        var difference = 0
        val chars = transactionSumInKopeks.toString().toCharArray()
        for (i in 0..chars.size / 2) {
            if (chars[i] != chars[chars.size - i - 1]) {
                difference++
            }
        }
        return difference <= 1
    }

    infix fun String.equalsIgnoreCase(other: String) = this.equals(other, true)
    infix fun Char.equalsIgnoreCase(other: Char) = this.equals(other, true)
    infix fun Double.multipleOf(value: Int) = this % value == 0.0

}

@RuleContext
class CashbackPercentResult(val transactionInfo: TransactionInfo) {
    var cashbackPercent = 0.0
    val firstNameLength = transactionInfo.firstName.length
    val lastNameLength = transactionInfo.lastName.length

    fun leastCommonMultiple(n: Int, m: Int): Double {
        return (abs(n * m) / greatestCommonDivisor(n, m)).toDouble()
    }

    private fun greatestCommonDivisor(n: Int, m: Int): Int {
        return if (n == m) n else greatestCommonDivisor(abs(n - m), min(n, m))
    }
}