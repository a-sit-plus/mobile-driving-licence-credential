package at.asitplus.wallet.mdl

import at.asitplus.iso.*
import at.asitplus.signum.indispensable.cosef.CoseHeader
import at.asitplus.signum.indispensable.cosef.CoseKey
import at.asitplus.signum.indispensable.cosef.CoseSigned
import at.asitplus.signum.indispensable.cosef.io.ByteStringWrapper
import at.asitplus.signum.indispensable.cosef.toCoseKey
import at.asitplus.testballoon.invoke
import at.asitplus.wallet.lib.agent.EphemeralKeyWithoutCert
import at.asitplus.wallet.lib.cbor.CoseHeaderCertificate
import at.asitplus.wallet.lib.cbor.SignCose
import at.asitplus.wallet.lib.cbor.VerifyCoseSignatureWithKey
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DOCUMENT_NUMBER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DRIVING_PRIVILEGES
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.EXPIRY_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.FAMILY_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.GIVEN_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.LocalDate
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlin.random.Random
import kotlin.time.Clock

val IsoMdocTest by testSuite {


    "issue and store and present and verify" {
        val wallet = Wallet()
        val verifier = Verifier()
        val issuer = Issuer()

        val deviceResponse = issuer.buildDeviceResponse(wallet.deviceKeyInfo)
        wallet.storeMdl(deviceResponse)

        val verifierRequest = verifier.buildDeviceRequest()
        val walletResponse = wallet.buildDeviceResponse(verifierRequest)
        verifier.verifyResponse(walletResponse, issuer.keyMaterial.publicKey.toCoseKey().getOrThrow())
    }

}

class Wallet {
    val keyMaterial = EphemeralKeyWithoutCert()

    val deviceKeyInfo = DeviceKeyInfo(
        deviceKey = keyMaterial.publicKey.toCoseKey().getOrThrow()
    )

    var storedMdl: MobileDrivingLicence? = null
    var storedIssuerAuth: CoseSigned<MobileSecurityObject>? = null
    var storedMdlItems: IssuerSignedList? = null

    fun storeMdl(deviceResponse: DeviceResponse) {
        val document = deviceResponse.documents?.first().shouldNotBeNull()
        document.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        val issuerAuth = document.issuerSigned.issuerAuth
        this.storedIssuerAuth = issuerAuth

        issuerAuth.payload.shouldNotBeNull()
        val mso = document.issuerSigned.issuerAuth.payload!!

        val mdlItems = document.issuerSigned.namespaces?.get(MobileDrivingLicenceScheme.isoNamespace).shouldNotBeNull()
        this.storedMdlItems = mdlItems
        mso.valueDigests[MobileDrivingLicenceScheme.isoNamespace].shouldNotBeNull()

        val givenNameValue = extractDataString(mdlItems, GIVEN_NAME)
        val familyNameValue = extractDataString(mdlItems, FAMILY_NAME)
        val licenceNumberValue = extractDataString(mdlItems, DOCUMENT_NUMBER)
        val issueDateValue = extractDataString(mdlItems, ISSUE_DATE)
        val expiryDateValue = extractDataString(mdlItems, EXPIRY_DATE)
        val drivingPrivilegesValue = extractDataDrivingPrivileges(mdlItems, DRIVING_PRIVILEGES)

        storedMdl = MobileDrivingLicence(
            familyName = familyNameValue,
            givenName = givenNameValue,
            licenceNumber = licenceNumberValue,
            portrait = byteArrayOf(),
            issueDate = LocalDate.parse(issueDateValue),
            expiryDate = LocalDate.parse(expiryDateValue),
            drivingPrivileges = drivingPrivilegesValue,
        )
    }

    suspend fun buildDeviceResponse(verifierRequest: DeviceRequest): DeviceResponse {
        val itemsRequest = verifierRequest.docRequests[0].itemsRequest
        val isoNamespace = itemsRequest.value.namespaces[MobileDrivingLicenceScheme.isoNamespace].shouldNotBeNull()
        val requestedKeys = isoNamespace.entries.filter { it.value }.map { it.key }

        return DeviceResponse(
            version = "1.0",
            documents = arrayOf(
                Document(
                    docType = MobileDrivingLicenceScheme.isoDocType,
                    issuerSigned = IssuerSigned.fromIssuerSignedItems(
                        namespacedItems = mapOf(
                            MobileDrivingLicenceScheme.isoNamespace to storedMdlItems!!.entries.filter {
                                it.value.elementIdentifier in requestedKeys
                            }.map { it.value }
                        ),
                        issuerAuth = storedIssuerAuth!!
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = ByteStringWrapper(DeviceNameSpaces(mapOf())),
                        deviceAuth = DeviceAuth(
                            deviceSignature = SignCose<ByteArray>(keyMaterial)(
                                null,
                                null,
                                byteArrayOf(),
                                ByteArraySerializer(),
                            ).getOrThrow()
                        )
                    )
                )
            ),
            status = 0U,
        )
    }

}

class Issuer {
    val keyMaterial = EphemeralKeyWithoutCert()

    suspend fun buildDeviceResponse(walletKeyInfo: DeviceKeyInfo): DeviceResponse {
        val drivingPrivilege = DrivingPrivilege(
            vehicleCategoryCode = "B",
            issueDate = LocalDate.parse("2023-01-01"),
            expiryDate = LocalDate.parse("2033-01-31"),
            codes = arrayOf(
                DrivingPrivilegeCode(code = "B", sign = "sign", value = "value")
            )
        )
        val issuerSigned = listOf(
            buildIssuerSignedItem(FAMILY_NAME, "Mustermann", 0U),
            buildIssuerSignedItem(GIVEN_NAME, "Max", 1U),
            buildIssuerSignedItem(DOCUMENT_NUMBER, "123456789", 2U),
            buildIssuerSignedItem(ISSUE_DATE, "2023-01-01", 3U),
            buildIssuerSignedItem(EXPIRY_DATE, "2033-01-31", 4U),
            buildIssuerSignedItem(DRIVING_PRIVILEGES, arrayOf(drivingPrivilege), 4U),
        )

        val mso = MobileSecurityObject(
            version = "1.0",
            digestAlgorithm = "SHA-256",
            valueDigests = mapOf(
                MobileDrivingLicenceScheme.isoNamespace to ValueDigestList(entries = issuerSigned.map {
                    ValueDigest.fromIssuerSignedItem(it, MobileDrivingLicenceScheme.isoNamespace)
                })
            ),
            deviceKeyInfo = walletKeyInfo,
            docType = MobileDrivingLicenceScheme.isoDocType,
            validityInfo = ValidityInfo(
                signed = Clock.System.now(),
                validFrom = Clock.System.now(),
                validUntil = Clock.System.now(),
            )
        )

        return DeviceResponse(
            version = "1.0",
            documents = arrayOf(
                Document(
                    docType = MobileDrivingLicenceScheme.isoDocType,
                    issuerSigned = IssuerSigned.fromIssuerSignedItems(
                        namespacedItems = mapOf(
                            MobileDrivingLicenceScheme.isoNamespace to issuerSigned
                        ),
                        issuerAuth = SignCose<MobileSecurityObject>(keyMaterial, null, CoseHeaderCertificate())(
                            null, null, mso, MobileSecurityObject.serializer(),
                        ).getOrThrow()
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = ByteStringWrapper(DeviceNameSpaces(mapOf())),
                        deviceAuth = DeviceAuth()
                    )
                )
            ),
            status = 0U,
        )
    }
}

class Verifier {
    val keyMaterial = EphemeralKeyWithoutCert()

    suspend fun buildDeviceRequest() = DeviceRequest(
        version = "1.0",
        docRequests = arrayOf(
            DocRequest(
                itemsRequest = ByteStringWrapper(
                    value = ItemsRequest(
                        docType = MobileDrivingLicenceScheme.isoDocType,
                        namespaces = mapOf(
                            MobileDrivingLicenceScheme.isoNamespace to ItemsRequestList(
                                listOf(
                                    SingleItemsRequest(FAMILY_NAME, true),
                                    SingleItemsRequest(GIVEN_NAME, true),
                                    SingleItemsRequest(PORTRAIT, false)
                                )
                            )
                        )
                    )
                ),
                readerAuth = SignCose<ByteArray>(keyMaterial)(
                    null,
                    CoseHeader(),
                    byteArrayOf(),
                    ByteArraySerializer(),
                ).getOrThrow()
            )
        )
    )

    suspend fun verifyResponse(deviceResponse: DeviceResponse, issuerKey: CoseKey) {
        val documents = deviceResponse.documents.shouldNotBeNull()
        val doc = documents.first()
        doc.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        doc.errors.shouldBeNull()
        val issuerSigned = doc.issuerSigned
        val issuerAuth = issuerSigned.issuerAuth
        VerifyCoseSignatureWithKey<MobileSecurityObject>()(
            issuerAuth,
            issuerKey,
            byteArrayOf(),
            null
        ).isSuccess shouldBe true
        issuerAuth.payload.shouldNotBeNull()
        val mso = issuerSigned.issuerAuth.payload!!

        mso.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        val mdlItems = mso.valueDigests[MobileDrivingLicenceScheme.isoNamespace].shouldNotBeNull()

        val walletKey = mso.deviceKeyInfo.deviceKey
        val deviceSignature = doc.deviceSigned.deviceAuth.deviceSignature.shouldNotBeNull()
        VerifyCoseSignatureWithKey<ByteArray>()(deviceSignature, walletKey, byteArrayOf(), null).isSuccess shouldBe true
        val namespaces = issuerSigned.namespaces.shouldNotBeNull()
        val issuerSignedItems = namespaces[MobileDrivingLicenceScheme.isoNamespace].shouldNotBeNull()

        extractAndVerifyData(issuerSignedItems, mdlItems, FAMILY_NAME)
        extractAndVerifyData(issuerSignedItems, mdlItems, GIVEN_NAME)
    }

    private fun extractAndVerifyData(
        issuerSignedItems: IssuerSignedList,
        mdlItems: ValueDigestList,
        key: String
    ) {
        val issuerSignedItem = issuerSignedItems.entries.first { it.value.elementIdentifier == key }
        //val elementValue = issuerSignedItem.value.elementValue.toString().shouldNotBeNull()
        val issuerHash = mdlItems.entries.first { it.key == issuerSignedItem.value.digestId }.shouldNotBeNull()
        val verifierHash = issuerSignedItem.serialized.sha256()
        verifierHash.encodeToString(Base16(strict = true)) shouldBe issuerHash.value.encodeToString(Base16(strict = true))
    }
}

private fun extractDataString(
    mdlItems: IssuerSignedList,
    key: String
): String {
    val element = mdlItems.entries.first { it.value.elementIdentifier == key }
    return element.value.elementValue.toString().shouldNotBeNull()
}

private fun extractDataDrivingPrivileges(
    mdlItems: IssuerSignedList,
    key: String
): Array<DrivingPrivilege> {
    val element = mdlItems.entries.first { it.value.elementIdentifier == key }
    return element.value.elementValue.shouldBeInstanceOf<Array<DrivingPrivilege>>()
}

fun buildIssuerSignedItem(elementIdentifier: String, elementValue: Any, digestId: UInt) = IssuerSignedItem(
    digestId = digestId,
    random = Random.nextBytes(16),
    elementIdentifier = elementIdentifier,
    elementValue = elementValue
)
