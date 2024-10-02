package at.asitplus.wallet.mdl

import at.asitplus.wallet.lib.iso.IssuerSignedItem
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_BIRTH_YEAR
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_IN_YEARS
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIRTH_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DRIVING_PRIVILEGES
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.EXPIRY_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.HEIGHT
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT_CAPTURE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.SEX
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.SIGNATURE_USUAL_MARK
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.WEIGHT
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import kotlin.random.Random
import kotlin.random.nextUInt

class SerializerRegistrationTest : FreeSpec({

    "Serialization and deserialization" - {
        withData(
            nameFn = {
                "for ${it.key}"
            },
            dataMap().entries
        ) {
            val item = IssuerSignedItem(
                digestId = Random.nextUInt(),
                random = Random.nextBytes(32),
                elementIdentifier = it.key,
                elementValue = it.value
            )

            val serialized = item.serialize(MobileDrivingLicenceScheme.isoNamespace)

            val deserialized =
                IssuerSignedItem.deserialize(serialized, MobileDrivingLicenceScheme.isoNamespace).getOrThrow()

            deserialized.elementValue shouldBe it.value
        }
    }

})

private fun dataMap(): Map<String, Any> =
    mapOf(
        BIRTH_DATE to randomLocalDate(),
        ISSUE_DATE to randomLocalDate(),
        EXPIRY_DATE to randomLocalDate(),
        PORTRAIT to Random.nextBytes(32),
        DRIVING_PRIVILEGES to arrayOf(
            DrivingPrivilege(
                vehicleCategoryCode = "A",
                issueDate = randomLocalDate(),
                expiryDate = randomLocalDate(),
            )
        ),
        SEX to IsoSexEnum.NOT_APPLICABLE,
        HEIGHT to Random.nextUInt(150u, 210u),
        WEIGHT to Random.nextUInt(60u, 120u),
        PORTRAIT_CAPTURE_DATE to randomLocalDate(),
        AGE_IN_YEARS to Random.nextUInt(1u, 99u),
        AGE_BIRTH_YEAR to Random.nextUInt(1900u, 2100u),
        SIGNATURE_USUAL_MARK to Random.nextBytes(32)
    )

private fun randomLocalDate() = LocalDate(Random.nextInt(1900, 2100), Random.nextInt(1, 12), Random.nextInt(1, 28))
