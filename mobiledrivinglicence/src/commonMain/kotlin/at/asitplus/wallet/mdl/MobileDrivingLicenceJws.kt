package at.asitplus.wallet.mdl

import at.asitplus.KmmResult.Companion.wrap
import at.asitplus.signum.indispensable.cosef.InstantLongSerializer
import at.asitplus.wallet.lib.data.NullableInstantLongSerializer
import at.asitplus.wallet.lib.data.vckJsonSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

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
) {

    fun serialize() = vckJsonSerializer.encodeToString(this)

    companion object {
        fun deserialize(it: String) = kotlin.runCatching {
            vckJsonSerializer.decodeFromString<MobileDrivingLicenceJws>(it)
        }.wrap()
    }

}