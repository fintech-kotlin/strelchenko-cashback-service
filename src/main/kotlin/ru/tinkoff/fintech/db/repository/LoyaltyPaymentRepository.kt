package ru.tinkoff.fintech.db.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity


interface LoyaltyPaymentRepository : JpaRepository<LoyaltyPaymentEntity, Long> {
    @Query("""select
            coalesce(sum(l.value), 0)
            from loyalty_payment l
            where 1=1 
                and l.card_id = ?1
                and date_trunc('month', current_date) = date_trunc('month', l.date_time) 
            group by l.card_id
            union
            select 0
            order by 1 desc limit 1""", nativeQuery = true)
    fun findCashbackTotalValueInCurrentMonth(cardId: String): Double
}
