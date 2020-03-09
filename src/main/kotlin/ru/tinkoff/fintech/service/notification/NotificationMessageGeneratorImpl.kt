package ru.tinkoff.fintech.service.notification

import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.NotificationMessageInfo

@Component
class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String {
        return """Уважаемый, ${notificationMessageInfo.name}!
                |Спешим Вам сообщить, что на карту ${cardNumberMasker.mask(notificationMessageInfo.cardNumber)}
                |начислен cashback в размере ${notificationMessageInfo.cashback}
                |за категорию ${notificationMessageInfo.category}.
                |Спасибо за покупку ${notificationMessageInfo.transactionDate}"""
            .trimMargin()
    }
}