package com.donghwan.team_reboott.common.lock

import com.donghwan.team_reboott.common.extension.logger
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager
import java.util.concurrent.TimeUnit

@Component
class DistributedLock(
    private val redissonClient: RedissonClient
) {

    private val log = logger()

    fun <T> lock(lockKey: LockKey, func: () -> T): T {
        val lock = redissonClient.getLock(lockKey.value)
        val acquired = lock.tryLock(lockKey.waitTimeSec, lockKey.leaseTimeSec, TimeUnit.SECONDS)
        if (!acquired) throw IllegalStateException("Lock not acquired: ${lockKey.value}")

        log.info("Lock acquired: ${lockKey.value}")

        return if (TransactionSynchronizationManager.isSynchronizationActive()) {
            lockWithTransaction(lockKey, lock, func)
        } else {
            lockImmediately(lockKey, lock, func)
        }
    }

    private fun <T> lockWithTransaction(lockKey: LockKey, lock: RLock, block: () -> T): T {
        TransactionSynchronizationManager.registerSynchronization(object : TransactionSynchronization {
            override fun afterCommit() {
                if (lock.isHeldByCurrentThread) {
                    lock.unlock()
                    log.info("Lock released after transaction commit: ${lockKey.value}")
                }
            }
        })

        return block()
    }

    private fun <T> lockImmediately(lockKey: LockKey, lock: RLock, block: () -> T): T {
        try {
            return block()
        } catch (e: Exception) {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
                log.info("Lock released on exception (no transaction): ${lockKey.value}")
            }
            throw e
        } finally {
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
                log.info("Lock released (no transaction): ${lockKey.value}")
            }
        }
    }
}

data class LockKey(
    val value: String,
    val waitTimeSec: Long = 30,
    val leaseTimeSec: Long = 3
) {

    companion object {
        fun companyCredit(companyId: Long) = of("company-credit", companyId)

        private fun of(prefix: String, id: Any): LockKey = LockKey("lock:$prefix:$id")
    }
}