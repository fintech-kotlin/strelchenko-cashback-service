package ru.tinkoff.fintech.service.cashback.dsl

import ru.tinkoff.fintech.service.cashback.*
import ru.tinkoff.fintech.service.cashback.dsl.DslRules.Companion.rules

val myRules =
    rules {
        rule(name = "BLACK") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BLACK }
                eval { cashbackTotalValue < MAX_CASH_BACK }
            } then {
                cashbackPercent = 1.0
            }
        }
        rule(name = "ALL") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_ALL }
                eval { mccCode == MCC_SOFTWARE }
                eval { transactionSumInKopeksIsPalindromeWithOneReplace }
            } then {
                cashbackPercent = leastCommonMultiple(firstNameLength, lastNameLength) / 1000
            }
        }
        rule(name = "BEER AND OLEG OLEGOV") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
                eval { firstName equalsIgnoreCase FIRST_NAME_OLEG }
                eval { lastName equalsIgnoreCase LAST_NAME_OLEGOV }
            } then {
                cashbackPercent = 10.0
            }
        }
        rule(name = "BEER AND OLEG") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
                eval { firstName equalsIgnoreCase FIRST_NAME_OLEG }
            } then {
                cashbackPercent = 7.0
            }
        }
        rule(name = "BEER AND FIRST LETTER NAME EQUALS FIRST LETTER CURRENT MONTH") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
                eval { firstLetterFirstName equalsIgnoreCase firstLetterCurrentMonth }
            } then {
                cashbackPercent = 5.0
            }
        }
        rule(name = "BEER AND FIRST LETTER NAME EQUALS FIRST LETTER PREVIOUS MONTH") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
                eval { firstLetterFirstName equalsIgnoreCase firstLetterPreviousMonth }
            } then {
                cashbackPercent = 3.0
            }
        }
        rule(name = "BEER AND FIRST LETTER NAME EQUALS FIRST LETTER NEXT MONTH") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
                eval { firstLetterFirstName equalsIgnoreCase firstLetterNextMonth }
            } then {
                cashbackPercent = 3.0
            }
        }
        rule(name = "BEER SIMPLE") {
            all {
                eval { loyaltyProgramName == LOYALTY_PROGRAM_BEER }
                eval { mccCode == MCC_BEER }
            } then {
                cashbackPercent = 2.0
            }
        }

        general_rule(name = "ROUNDING RULE") {
            then {
                roundCashback()
            }
        }
        general_rule(name = "666") {
            all {
                eval { transactionSum multipleOf(666) }
            } then {
                add(6.66)
            }
        }
        general_rule(name = "LIMIT RULE") {
            then {
                limit(MAX_CASH_BACK)
            }
        }
    }