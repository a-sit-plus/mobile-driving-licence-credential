package at.asitplus.wallet.mdl

import at.asitplus.KmmResult.Companion.wrap
import at.asitplus.wallet.lib.data.vckJsonSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

/**
 * JWS representation of a [MobileDrivingLicence].
 */
@Serializable
data class MobileDrivingLicenceJwsNamespace(
    @SerialName("org.iso.18013.5.1")
    val mdl: MobileDrivingLicence,
) {

    fun serialize() = vckJsonSerializer.encodeToString(this)

    companion object {
        fun deserialize(it: String) = kotlin.runCatching {
            vckJsonSerializer.decodeFromString<MobileDrivingLicenceJwsNamespace>(it)
        }.wrap()
    }

}