package ru.tinkoff.fintech.listener

import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.transaction.TransactionService

@Service
class TransactionListener(
    private val transactionService: TransactionService
) {
    companion object : KLogging()

    @KafkaListener(topics = ["\${spring.kafka.consumer.topic}"])
    fun onMessage(transaction: Transaction) {
        logger.info("Start handle transaction: $transaction")
        try {
            transactionService.handleTransaction(transaction)
            logger.info("Transaction $transaction handled successfully")
        } catch (e: Exception) {
            logger.error("Transaction $transaction not handled. Got error: $e")
        }
    }
}


