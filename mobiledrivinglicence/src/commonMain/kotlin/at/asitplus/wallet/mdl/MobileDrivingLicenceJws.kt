package at.asitplus.wallet.mdl

import at.asitplus.signum.indispensable.cosef.InstantLongSerializer
import at.asitplus.wallet.lib.data.NullableInstantLongSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * JWS representation of a [MobileDrivingLicence], used e.g. in the payload of a JWS in a single
 * instance of [ServerResponse.documents]
 */
@Serializable
data class MobileDrivingLicenceJws(
    @SerialName("doctype")
    val doctype: String,
    @SerialName("namespaces")
    val namespaces: MobileDrivingLicenceJwsNamespace,
    @SerialName("iat")
    @Serializable(with = InstantLongSerializer::class)
    val issuedAt: Instant,
    @SerialName("exp")
    @Serializable(with = NullableInstantLongSerializer::class)
    val expiration: Instant?,
)