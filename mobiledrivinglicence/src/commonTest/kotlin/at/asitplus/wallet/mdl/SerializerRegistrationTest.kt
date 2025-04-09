package at.asitplus.wallet.mdl

import at.asitplus.signum.indispensable.CryptoSignature
import at.asitplus.signum.indispensable.cosef.CoseEllipticCurve
import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseKey
import at.asitplus.signum.indispensable.cosef.CoseKeyParams
import at.asitplus.signum.indispensable.cosef.CoseKeyType
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.CredentialToJsonConverter
import at.asitplus.wallet.lib.iso.DeviceKeyInfo
import at.asitplus.wallet.lib.iso.IssuerSigned
import at.asitplus.wallet.lib.iso.IssuerSignedItem
import at.asitplus.wallet.lib.iso.MobileSecurityObject
import at.asitplus.wallet.lib.iso.ValidityInfo
import at.asitplus.wallet.lib.iso.ValueDigest
import at.asitplus.wallet.lib.iso.ValueDigestList
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ADMINISTRATIVE_NUMBER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_BIRTH_YEAR
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_IN_YEARS
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_12
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_13
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_14
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_16
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_18
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_21
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_25
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_60
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_62
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_65
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.AGE_OVER_68
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIOMETRIC_TEMPLATE_FACE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIOMETRIC_TEMPLATE_FINGER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIOMETRIC_TEMPLATE_IRIS
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.BIOMETRIC_TEMPLATE_SIGNATURE_SIGN
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random
import kotlin.random.nextUInt

class SerializerRegistrationTest : FreeSpec({

    "Serialization and deserialization" - {
        withData(nameFn = { " for ${it.key}" }, dataMap().entries) {

            val item = it.toIssuerSignedItem()
            val serialized = item.serialize(MobileDrivingLicenceScheme.isoNamespace)

            val deserialized = IssuerSignedItem.deserialize(serialized, MobileDrivingLicenceScheme.isoNamespace,item.elementIdentifier)
                .getOrThrow()

            deserialized.elementValue shouldBe it.value
        }
    }

    "Serialization to JSON Element" {

        val mso = MobileSecurityObject(
            version = "1.0",
            digestAlgorithm = "SHA-256",
            valueDigests = mapOf("foo" to ValueDigestList(listOf(ValueDigest(0U, byteArrayOf())))),
            deviceKeyInfo = deviceKeyInfo(),
            docType = "docType",
            validityInfo = ValidityInfo(Clock.System.now(), Clock.System.now(), Clock.System.now())
        )

        val claims = dataMap()
        val namespacedItems: Map<String, List<IssuerSignedItem>> =
            mapOf(MobileDrivingLicenceScheme.isoNamespace to claims.map { it.toIssuerSignedItem() }.toList())
        val issuerAuth = CoseSigned.create(CoseHeader(),null, mso, CryptoSignature.RSA(byteArrayOf(1,3,3,7)),
            MobileSecurityObject.serializer()
        )
        val credential = SubjectCredentialStore.StoreEntry.Iso(
            IssuerSigned.fromIssuerSignedItems(namespacedItems, issuerAuth),
            MobileDrivingLicenceScheme.isoNamespace
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
        AGE_OVER_12 to Random.nextBoolean(),
        AGE_OVER_13 to Random.nextBoolean(),
        AGE_OVER_14 to Random.nextBoolean(),
        AGE_OVER_16 to Random.nextBoolean(),
        AGE_OVER_18 to Random.nextBoolean(),
        AGE_OVER_21 to Random.nextBoolean(),
        AGE_OVER_25 to Random.nextBoolean(),
        AGE_OVER_60 to Random.nextBoolean(),
        AGE_OVER_62 to Random.nextBoolean(),
        AGE_OVER_65 to Random.nextBoolean(),
        AGE_OVER_68 to Random.nextBoolean(),
        ISSUING_JURISDICTION to randomString(),
        NATIONALITY to randomString(),
        RESIDENT_CITY to randomString(),
        RESIDENT_STATE to randomString(),
        RESIDENT_POSTAL_CODE to randomString(),
        RESIDENT_COUNTRY to randomString(),
        FAMILY_NAME_NATIONAL_CHARACTER to randomString(),
        GIVEN_NAME_NATIONAL_CHARACTER to randomString(),
        SIGNATURE_USUAL_MARK to Random.nextBytes(32),
        BIOMETRIC_TEMPLATE_FACE to Random.nextBytes(32),
        BIOMETRIC_TEMPLATE_FINGER to Random.nextBytes(32),
        BIOMETRIC_TEMPLATE_SIGNATURE_SIGN to Random.nextBytes(32),
        BIOMETRIC_TEMPLATE_IRIS to Random.nextBytes(32),
    )

private fun randomString() = Random.nextBytes(16).encodeToString(Base64())

private fun randomLocalDate() = LocalDate(Random.nextInt(1900, 2100), Random.nextInt(1, 12), Random.nextInt(1, 28))


private fun deviceKeyInfo() =
    DeviceKeyInfo(CoseKey(CoseKeyType.EC2, keyParams = CoseKeyParams.EcYBoolParams(CoseEllipticCurve.P256)))
