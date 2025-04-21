package com.donghwan.team_reboott.common.lock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.util.concurrent.TimeUnit

@SpringBootTest
class DistributedLockTest @Autowired constructor(
    private val distributedLock: DistributedLock,
    private val redissonClient: RedissonClient,
    private val transactionManager: PlatformTransactionManager
) {

    @Nested
    inner class Lock {

        @Test
        fun success_lock_without_transaction_should_release_immediately() {
            // given
            val lockKey = LockKey("lock:test:no-tx", waitTimeSec = 2, leaseTimeSec = 5)

            // when
            val result = distributedLock.lock(lockKey) {
                "locked-no-tx"
            }

            // then
            assertThat(result).isEqualTo("locked-no-tx")
            val lock = redissonClient.getLock(lockKey.value)
            assertThat(lock.isLocked).isFalse()
        }

        @Test
        fun success_lock_with_manual_transaction_commit() {
            // given
            val lockKey = LockKey("lock:test:manual-tx", waitTimeSec = 2, leaseTimeSec = 5)
            val tx = transactionManager.getTransaction(DefaultTransactionDefinition())

            val result = distributedLock.lock(lockKey) {
                "locked-in-tx"
            }

            assertThat(result).isEqualTo("locked-in-tx")

            // 트랜잭션 커밋 → unlock should happen via afterCommit()
            transactionManager.commit(tx)

            // then
            val lock = redissonClient.getLock(lockKey.value)
            assertThat(lock.isLocked).isFalse()
        }

        @Test
        fun success_lock_with_exception_should_still_release() {
            // given
            val lockKey = LockKey("lock:test:exception", waitTimeSec = 2, leaseTimeSec = 3)

            // when
            assertThrows<IllegalStateException> {
                distributedLock.lock(lockKey) {
                    throw IllegalStateException("expected failure")
                }
            }

            // then
            val lock = redissonClient.getLock(lockKey.value)
            assertThat(lock.isLocked).isFalse()
        }

        @Test
        fun failure_lock_when_already_locked() {
            val lockKey = LockKey("lock:test:conflict-thread", waitTimeSec = 1, leaseTimeSec = 10)
            val competingLock = redissonClient.getLock(lockKey.value)

            val thread = Thread {
                competingLock.lock(10, TimeUnit.SECONDS)
                Thread.sleep(3000) // 블록 유지 시간
                competingLock.unlock()
            }
            thread.start()

            // 락 선점될 시간 대기
            Thread.sleep(500)

            val ex = assertThrows<IllegalStateException> {
                distributedLock.lock(lockKey) {
                    "this should not be executed"
                }
            }

            assertThat(ex.message).contains("Lock not acquired")

            thread.join()
        }
    }
}