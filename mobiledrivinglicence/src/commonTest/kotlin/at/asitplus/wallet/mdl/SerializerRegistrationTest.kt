package at.asitplus.wallet.mdl

import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.signum.indispensable.cosef.io.ByteStringWrapper
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.CredentialToJsonConverter
import at.asitplus.wallet.lib.iso.IssuerSigned
import at.asitplus.wallet.lib.iso.IssuerSignedItem
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ADMINISTRATIVE_NUMBER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_BIRTH_YEAR
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_IN_YEARS
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_18
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIRTH_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIRTH_PLACE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DOCUMENT_NUMBER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DRIVING_PRIVILEGES
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.EXPIRY_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.EYE_COLOUR
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.FAMILY_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.FAMILY_NAME_NATIONAL_CHARACTER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.GIVEN_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.GIVEN_NAME_NATIONAL_CHARACTER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.HAIR_COLOUR
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.HEIGHT
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUING_AUTHORITY
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUING_COUNTRY
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUING_JURISDICTION
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.NATIONALITY
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT_CAPTURE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.RESIDENT_ADDRESS
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.RESIDENT_CITY
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.RESIDENT_COUNTRY
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.RESIDENT_POSTAL_CODE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.RESIDENT_STATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.SEX
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.SIGNATURE_USUAL_MARK
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.UN_DISTINGUISHING_SIGN
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.WEIGHT
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.matthewnelson.encoding.base64.Base64
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random
import kotlin.random.nextUInt

class SerializerRegistrationTest : FreeSpec({

    "Serialization and deserialization" - {
        withData(nameFn = { " for ${it.key}" }, dataMap().entries) {

            val serialized = it.toIssuerSignedItem().serialize(MobileDrivingLicenceScheme.isoNamespace)

            val deserialized = IssuerSignedItem.deserialize(serialized, MobileDrivingLicenceScheme.isoNamespace)
                .getOrThrow()

            deserialized.elementValue shouldBe it.value
        }
    }

    "Serialization to JSON Element" {
        val claims = dataMap()
        val namespacedItems: Map<String, List<IssuerSignedItem>> =
            mapOf(MobileDrivingLicenceScheme.isoNamespace to claims.map { it.toIssuerSignedItem() }.toList())
        val issuerAuth = CoseSigned(ByteStringWrapper(CoseHeader()), null, null, byteArrayOf())
        val credential = SubjectCredentialStore.StoreEntry.Iso(
            IssuerSigned.fromIssuerSignedItems(namespacedItems, issuerAuth),
            MobileDrivingLicenceScheme
        )
        val converted = CredentialToJsonConverter.toJsonElement(credential)
            .shouldBeInstanceOf<JsonObject>()
        val jsonMap = converted[MobileDrivingLicenceScheme.isoNamespace]
            .shouldBeInstanceOf<JsonObject>()

        claims.forEach {
            withClue("Serialization for ${it.key}") {
                jsonMap[it.key].shouldNotBeNull()
            }
        }
    }

})

private fun Map.Entry<String, Any>.toIssuerSignedItem() =
    IssuerSignedItem(Random.nextUInt(), Random.nextBytes(32), key, value)

private fun dataMap(): Map<String, Any> =
    mapOf(
        FAMILY_NAME to randomString(),
        GIVEN_NAME to randomString(),
        BIRTH_DATE to randomLocalDate(),
        ISSUE_DATE to randomLocalDate(),
        EXPIRY_DATE to randomLocalDate(),
        ISSUING_COUNTRY to randomString(),
        ISSUING_AUTHORITY to randomString(),
        DOCUMENT_NUMBER to randomString(),
        PORTRAIT to Random.nextBytes(32),
        DRIVING_PRIVILEGES to arrayOf(
            DrivingPrivilege(
                vehicleCategoryCode = randomString(),
                issueDate = randomLocalDate(),
                expiryDate = randomLocalDate(),
            )
        ),
        UN_DISTINGUISHING_SIGN to randomString(),
        ADMINISTRATIVE_NUMBER to randomString(),
        SEX to IsoSexEnum.NOT_APPLICABLE,
        HEIGHT to Random.nextUInt(150u, 210u),
        WEIGHT to Random.nextUInt(60u, 120u),
        EYE_COLOUR to randomString(),
        HAIR_COLOUR to randomString(),
        BIRTH_PLACE to randomString(),
        RESIDENT_ADDRESS to randomString(),
        PORTRAIT_CAPTURE_DATE to randomLocalDate(),
        AGE_IN_YEARS to Random.nextUInt(1u, 99u),
        AGE_BIRTH_YEAR to Random.nextUInt(1900u, 2100u),
        AGE_OVER_18 to true,
        ISSUING_JURISDICTION to randomString(),
        NATIONALITY to randomString(),
        RESIDENT_CITY to randomString(),
        RESIDENT_STATE to randomString(),
        RESIDENT_POSTAL_CODE to randomString(),
        RESIDENT_COUNTRY to randomString(),
        FAMILY_NAME_NATIONAL_CHARACTER to randomString(),
        GIVEN_NAME_NATIONAL_CHARACTER to randomString(),
        SIGNATURE_USUAL_MARK to Random.nextBytes(32),
    )

private fun randomString() = Random.nextBytes(16).encodeToString(Base64())

private fun randomLocalDate() = LocalDate(Random.nextInt(1900, 2100), Random.nextInt(1, 12), Random.nextInt(1, 28))
