import java.time.LocalDateTime

data class AiFeatureUsageProjectionDto(
    val companyId: Long,
    val companyName: String,
    val featureId: Long,
    val featureName: String,
    val usedCredit: Long,
    val inputLength: Int,
    val usedAt: LocalDateTime
)