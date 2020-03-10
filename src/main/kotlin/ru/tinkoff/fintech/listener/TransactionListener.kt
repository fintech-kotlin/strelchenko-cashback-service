package ru.tinkoff.fintech.listener

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.transaction.TransactionService

@Service
class TransactionListener(
    private val transactionService: TransactionService
) {

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(transaction: Transaction) {
        transactionService.handleTransaction(transaction)
    }
}


