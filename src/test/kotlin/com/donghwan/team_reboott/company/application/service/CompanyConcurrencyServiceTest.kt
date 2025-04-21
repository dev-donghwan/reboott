package com.donghwan.team_reboott.company.application.service

import com.donghwan.team_reboott.company.application.dto.ChargeCompanyCreditCommand
import com.donghwan.team_reboott.company.domain.model.Company
import com.donghwan.team_reboott.company.domain.model.CompanyCredit
import com.donghwan.team_reboott.company.domain.repository.CompanyRepository
import com.donghwan.team_reboott.company.infra.jpa.CompanyCreditJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CompletableFuture

@SpringBootTest
class CompanyServiceConcurrencyTest @Autowired constructor(
    private val companyService: CompanyService,
    private val companyRepository: CompanyRepository,
    private val creditRepository: CompanyCreditJpaRepository
) {

    @Test
    fun success() {
        // given
        val initialCredit = creditRepository.save(CompanyCredit.create(0))
        val company = companyRepository.save(Company.create("test", initialCredit))

        val threadCount = 10
        val chargeAmount = 100L

        // when
        val futures = (1..threadCount).map {
            CompletableFuture.runAsync {
                companyService.chargeCredit(ChargeCompanyCreditCommand(company.id, chargeAmount))
            }
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()

        // then
        val updated = companyRepository.getById(company.id)
        assertThat(updated.credit.amount).isEqualTo(chargeAmount * threadCount)
    }
}