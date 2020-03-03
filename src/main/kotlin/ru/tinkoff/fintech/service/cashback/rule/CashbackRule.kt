package ru.tinkoff.fintech.service.cashback.rule

interface CashbackRule {
    fun useThisRule(): Boolean
    fun calculateCashbackPercent(): Double
}