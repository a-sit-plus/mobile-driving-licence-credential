package at.asitplus.wallet.mdl

import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.signum.indispensable.cosef.io.ByteStringWrapper
import at.asitplus.wallet.lib.agent.SubjectCredentialStore
import at.asitplus.wallet.lib.data.CredentialToJsonConverter
import at.asitplus.wallet.lib.iso.IssuerSigned
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
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.JsonObject
import kotlin.random.Random
import kotlin.random.nextUInt

class SerializerRegistrationTest : FreeSpec({

    "Serialization and deserialization" - {
        withData(nameFn = { " for ${it.key}" }, dataMap().entries) {
            val item = IssuerSignedItem(
                digestId = Random.nextUInt(),
                random = Random.nextBytes(32),
                elementIdentifier = it.key,
                elementValue = it.value
            )

            val serialized = item.serialize(MobileDrivingLicenceScheme.isoNamespace)

            val deserialized = IssuerSignedItem.deserialize(serialized, MobileDrivingLicenceScheme.isoNamespace)
                .getOrThrow()

            deserialized.elementValue shouldBe it.value
        }
    }

    "Serialization to JSON Element" {
        val claims = dataMap()
        val namespacedItems: Map<String, List<IssuerSignedItem>> =
            mapOf(MobileDrivingLicenceScheme.isoNamespace to claims.map {
                IssuerSignedItem(Random.nextUInt(), Random.nextBytes(32), it.key, it.value)
            }.toList())
        val issuerAuth = CoseSigned(ByteStringWrapper(CoseHeader()), null, null, byteArrayOf())
        val credential = SubjectCredentialStore.StoreEntry.Iso(
            IssuerSigned.fromIssuerSignedItems(namespacedItems, issuerAuth),
            MobileDrivingLicenceScheme
        )
        val converted = CredentialToJsonConverter.toJsonElement(credential)
            .shouldBeInstanceOf<JsonObject>()
            .also { println(it) }
        val jsonMap = converted[MobileDrivingLicenceScheme.isoNamespace]
            .shouldBeInstanceOf<JsonObject>()

        claims.forEach {
            withClue("Serialization for ${it.key}") {
                jsonMap[it.key].shouldNotBeNull()
            }
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
