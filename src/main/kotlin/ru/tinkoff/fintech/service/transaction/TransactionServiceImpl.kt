package ru.tinkoff.fintech.service.transaction

import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.CardServiceClient
import ru.tinkoff.fintech.client.ClientService
import ru.tinkoff.fintech.client.LoyaltyServiceClient
import ru.tinkoff.fintech.client.NotificationServiceClient
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import ru.tinkoff.fintech.model.*
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.notification.NotificationMessageGenerator

@Service
class TransactionServiceImpl(
    private val cardService: CardServiceClient,
    private val clientService: ClientService,
    private val loyaltyService: LoyaltyServiceClient,
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository,
    private val notificationMessageGenerator: NotificationMessageGenerator,
    private val cashbackCalculator: CashbackCalculator,
    private val notificationService: NotificationServiceClient,
    @Value("\${loyalty.payment.sign}")
    private val loyaltyPaymentSign: String
) : TransactionService {

    override fun handleTransaction(transaction: Transaction) {
        runBlocking(Dispatchers.IO) {
            val card = cardService.getCard(transaction.cardNumber)
            val clientFuture = async { clientService.getClient(card.client) }
            val loyaltyProgramFuture = async { loyaltyService.getLoyaltyProgram(card.loyaltyProgram) }
            val totalCashbackInCurrentMonthFuture = async { loyaltyPaymentRepository.findCashbackTotalValueInCurrentMonth(card.id) }

            val client = clientFuture.await()
            val loyaltyProgram = loyaltyProgramFuture.await()
            val totalCashbackInCurrentMonth = totalCashbackInCurrentMonthFuture.await()

            val transactionInfo = TransactionInfo(
                loyaltyProgram.name,
                transaction.value,
                totalCashbackInCurrentMonth,
                transaction.mccCode,
                client.birthDate,
                client.firstName,
                client.lastName,
                client.middleName
            )
            val cashback = cashbackCalculator.calculateCashback(transactionInfo)

            // В ТЗ не сказано, додумал сам, что если не операция покупки, то кэшбек начислять не надо
            if (cashback == 0.0 || transactionInfo.mccCode == null) this.cancel()

            launch { saveLoyaltyPaymentInfo(card, cashback, transaction) }
            launch { sendNotification(transactionInfo, transaction, cashback, client) }
        }
    }

    private fun sendNotification(
        transactionInfo: TransactionInfo,
        transaction: Transaction,
        cashback: Double,
        client: Client
    ) {
        val messageInfo = NotificationMessageInfo(
            transactionInfo.firstName,
            transaction.cardNumber,
            cashback,
            transaction.value,
            transactionInfo.loyaltyProgramName,
            transaction.time
        )
        val generatedMessage = notificationMessageGenerator.generateMessage(messageInfo)
        notificationService.sendNotification(client.id, generatedMessage)
    }

    private fun saveLoyaltyPaymentInfo(card: Card, cashback: Double, transaction: Transaction) {
        val loyaltyPayment = LoyaltyPaymentEntity(
            value = cashback,
            cardId = card.id,
            sign = loyaltyPaymentSign,
            transactionId = transaction.transactionId,
            dateTime = transaction.time
        )
        loyaltyPaymentRepository.save(loyaltyPayment)
    }
}