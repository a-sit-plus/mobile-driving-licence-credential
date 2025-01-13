package at.asitplus.wallet.mdl

import at.asitplus.wallet.lib.JsonValueEncoder
import at.asitplus.wallet.lib.LibraryInitializer
import at.asitplus.wallet.lib.data.vckJsonSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.encodeToJsonElement

object Initializer {

    /**
     * A reference to this class is enough to trigger the init block
     */
    init {
        initWithVCK()
    }

    /**
     * This has to be called first, before anything, to load the
     * relevant classes' serializer's of this library into the base implementations of VC-K
     */
    fun initWithVCK() {
        LibraryInitializer.registerExtensionLibrary(
            credentialScheme = MobileDrivingLicenceScheme,
            jsonValueEncoder = jsonValueEncoder(),
            itemValueSerializerMap = mapOf(
                MobileDrivingLicenceDataElements.BIRTH_DATE to LocalDate.serializer(),
                MobileDrivingLicenceDataElements.ISSUE_DATE to LocalDate.serializer(),
                MobileDrivingLicenceDataElements.EXPIRY_DATE to LocalDate.serializer(),
                MobileDrivingLicenceDataElements.PORTRAIT to ByteArraySerializer(),
                MobileDrivingLicenceDataElements.DRIVING_PRIVILEGES to ArraySerializer(DrivingPrivilege.serializer()),
                MobileDrivingLicenceDataElements.SEX to IsoSexEnumSerializer,
                MobileDrivingLicenceDataElements.HEIGHT to UInt.serializer(),
                MobileDrivingLicenceDataElements.WEIGHT to UInt.serializer(),
                MobileDrivingLicenceDataElements.PORTRAIT_CAPTURE_DATE to LocalDate.serializer(),
                MobileDrivingLicenceDataElements.AGE_IN_YEARS to UInt.serializer(),
                MobileDrivingLicenceDataElements.AGE_BIRTH_YEAR to UInt.serializer(),
                MobileDrivingLicenceDataElements.SIGNATURE_USUAL_MARK to ByteArraySerializer(),
                MobileDrivingLicenceDataElements.AGE_OVER_12 to Boolean.serializer(),
                MobileDrivingLicenceDataElements.AGE_OVER_14 to Boolean.serializer(),
                MobileDrivingLicenceDataElements.AGE_OVER_16 to Boolean.serializer(),
                MobileDrivingLicenceDataElements.AGE_OVER_18 to Boolean.serializer(),
                MobileDrivingLicenceDataElements.AGE_OVER_21 to Boolean.serializer(),
            )
        )
    }

    private fun jsonValueEncoder(): JsonValueEncoder = {
        when (it) {
            is DrivingPrivilege -> vckJsonSerializer.encodeToJsonElement(it)
            is LocalDate -> vckJsonSerializer.encodeToJsonElement(it)
            is UInt -> vckJsonSerializer.encodeToJsonElement(it)
            else -> null
        }
    }

}
