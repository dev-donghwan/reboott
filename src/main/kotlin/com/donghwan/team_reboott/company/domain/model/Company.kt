package com.donghwan.team_reboott.company.domain.model

import com.donghwan.team_reboott.aifeaturebundle.domain.model.AiFeatureBundle
import com.donghwan.team_reboott.common.exception.GlobalException
import com.donghwan.team_reboott.common.response.ErrorCode
import jakarta.persistence.*

@Entity
class Company private constructor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val _id: Long? = null,
    val name: String,
    @OneToOne @JoinColumn(name = "credit_id")
    val credit: CompanyCredit,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "bundle_id")
    private var _bundle: AiFeatureBundle? = null
) {
    val id: Long
        get() = _id ?: throw GlobalException(ErrorCode.ID_INITIALIZE)

    val bundle: AiFeatureBundle
        get() = _bundle ?: throw GlobalException(ErrorCode.ENTITY_NOT_ASSIGNED)

    fun hasBundle(): Boolean = _bundle != null

    fun assignBundle(bundle: AiFeatureBundle) {
        _bundle = bundle
    }

    companion object {
        fun create(name: String, credit: CompanyCredit) = Company(
            _id = null,
            name = name,
            credit = credit
        )
    }
}