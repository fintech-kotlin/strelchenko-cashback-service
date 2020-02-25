package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl : CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        require(end >= start) { "Start index cannot be greater than end index" }

        val result = StringBuilder()
        cardNumber.forEachIndexed { index, c ->
            if (index in start until end) {
                result.append(maskChar)
            } else {
                result.append(c)
            }
        }
        return result.toString()
    }
}