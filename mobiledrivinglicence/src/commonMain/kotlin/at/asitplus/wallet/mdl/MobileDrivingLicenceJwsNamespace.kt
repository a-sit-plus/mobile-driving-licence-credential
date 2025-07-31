package at.asitplus.wallet.mdl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JWS representation of a [MobileDrivingLicence].
 */
@Serializable
data class MobileDrivingLicenceJwsNamespace(
    @SerialName("org.iso.18013.5.1")
    val mdl: MobileDrivingLicence,
)