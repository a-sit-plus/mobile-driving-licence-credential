package at.asitplus.wallet.mdl

import at.asitplus.crypto.datatypes.cose.CoseHeader
import at.asitplus.crypto.datatypes.cose.CoseKey
import at.asitplus.crypto.datatypes.cose.CoseSigned
import at.asitplus.wallet.lib.agent.DefaultCryptoService
import at.asitplus.wallet.lib.agent.RandomKeyPairAdapter
import at.asitplus.wallet.lib.cbor.DefaultCoseService
import at.asitplus.wallet.lib.cbor.DefaultVerifierCoseService
import at.asitplus.wallet.lib.iso.DeviceAuth
import at.asitplus.wallet.lib.iso.DeviceKeyInfo
import at.asitplus.wallet.lib.iso.DeviceRequest
import at.asitplus.wallet.lib.iso.DeviceResponse
import at.asitplus.wallet.lib.iso.DeviceSigned
import at.asitplus.wallet.lib.iso.DocRequest
import at.asitplus.wallet.lib.iso.Document
import at.asitplus.wallet.lib.iso.IssuerSigned
import at.asitplus.wallet.lib.iso.IssuerSignedItem
import at.asitplus.wallet.lib.iso.IssuerSignedList
import at.asitplus.wallet.lib.iso.ItemsRequest
import at.asitplus.wallet.lib.iso.ItemsRequestList
import at.asitplus.wallet.lib.iso.MobileSecurityObject
import at.asitplus.wallet.lib.iso.SingleItemsRequest
import at.asitplus.wallet.lib.iso.ValidityInfo
import at.asitplus.wallet.lib.iso.ValueDigest
import at.asitplus.wallet.lib.iso.ValueDigestList
import at.asitplus.wallet.lib.iso.sha256
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DOCUMENT_NUMBER
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.DRIVING_PRIVILEGES
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.EXPIRY_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.FAMILY_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.GIVEN_NAME
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.ISSUE_DATE
import at.asitplus.wallet.mdl.MobileDrivingLicenceDataElements.PORTRAIT
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.serialization.cbor.ByteStringWrapper
import kotlin.random.Random

class IsoMdocTest : FreeSpec({

    "issue, store, present, verify" {
        val wallet = Wallet()
        val verifier = Verifier()
        val issuer = Issuer()

        // TODO Wallet needs to prove possession of key
        val deviceResponse = issuer.buildDeviceResponse(wallet.deviceKeyInfo)
        wallet.storeMdl(deviceResponse)

        val verifierRequest = verifier.buildDeviceRequest()
        val walletResponse = wallet.buildDeviceResponse(verifierRequest)
        issuer.cryptoService.keyPairAdapter.coseKey shouldNotBe null
        verifier.verifyResponse(walletResponse, issuer.cryptoService.keyPairAdapter.coseKey)
    }

})

class Wallet {

    val cryptoService = DefaultCryptoService(RandomKeyPairAdapter())
    val coseService = DefaultCoseService(cryptoService)

    val deviceKeyInfo = DeviceKeyInfo(
        deviceKey = cryptoService.keyPairAdapter.coseKey
    )

    var storedMdl: MobileDrivingLicence? = null
    var storedIssuerAuth: CoseSigned? = null
    var storedMdlItems: IssuerSignedList? = null

    fun storeMdl(deviceResponse: DeviceResponse) {
        val document = deviceResponse.documents?.firstOrNull()
        document.shouldNotBeNull()
        document.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        val issuerAuth = document.issuerSigned.issuerAuth
        this.storedIssuerAuth = issuerAuth
        println("Wallet stored IssuerAuth: $issuerAuth")
        val issuerAuthPayload = issuerAuth.payload
        issuerAuthPayload.shouldNotBeNull()
        val mso = document.issuerSigned.getIssuerAuthPayloadAsMso()
        mso.shouldNotBeNull()
        val mdlItems = document.issuerSigned.namespaces?.get(MobileDrivingLicenceScheme.isoNamespace)
        mdlItems.shouldNotBeNull()
        this.storedMdlItems = mdlItems
        val valueDigests = mso.valueDigests[MobileDrivingLicenceScheme.isoNamespace]
        valueDigests.shouldNotBeNull()

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
            drivingPrivileges = drivingPrivilegesValue.toList(),
        )
        println("Wallet stored MDL: $storedMdl")
    }

    suspend fun buildDeviceResponse(verifierRequest: DeviceRequest): DeviceResponse {
        val isoNamespace =
            verifierRequest.docRequests[0].itemsRequest.value.namespaces[MobileDrivingLicenceScheme.isoNamespace]
        isoNamespace.shouldNotBeNull()
        val requestedKeys = isoNamespace.entries.filter { it.value }.map { it.key }
        return DeviceResponse(
            version = "1.0",
            documents = arrayOf(
                Document(
                    docType = MobileDrivingLicenceScheme.isoDocType,
                    issuerSigned = IssuerSigned(
                        namespaces = mapOf(
                            MobileDrivingLicenceScheme.isoNamespace to IssuerSignedList(storedMdlItems!!.entries.filter {
                                it.value.elementIdentifier in requestedKeys
                            })
                        ),
                        issuerAuth = storedIssuerAuth!!
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = byteArrayOf(),
                        deviceAuth = DeviceAuth(
                            deviceSignature = coseService.createSignedCose(
                                payload = null,
                                addKeyId = false
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

    val cryptoService = DefaultCryptoService(RandomKeyPairAdapter())
    val coseService = DefaultCoseService(cryptoService)

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
                    ValueDigest.fromIssuerSigned(it)
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
                    issuerSigned = IssuerSigned(
                        namespaces = mapOf(
                            MobileDrivingLicenceScheme.isoNamespace to IssuerSignedList.withItems(
                                issuerSigned
                            )
                        ),
                        issuerAuth = coseService.createSignedCose(
                            payload = mso.serializeForIssuerAuth(),
                            addKeyId = false,
                            addCertificate = true,
                        ).getOrThrow()
                    ),
                    deviceSigned = DeviceSigned(
                        namespaces = byteArrayOf(),
                        deviceAuth = DeviceAuth()
                    )
                )
            ),
            status = 0U,
        )
    }
}

class Verifier {

    val cryptoService = DefaultCryptoService(RandomKeyPairAdapter())
    val coseService = DefaultCoseService(cryptoService)
    val verifierCoseService = DefaultVerifierCoseService()

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
                readerAuth = coseService.createSignedCose(
                    unprotectedHeader = CoseHeader(),
                    payload = null,
                    addKeyId = false,
                ).getOrThrow()
            )
        )
    )

    fun verifyResponse(deviceResponse: DeviceResponse, issuerKey: CoseKey) {
        val documents = deviceResponse.documents
        documents.shouldNotBeNull()
        val doc = documents.first()
        doc.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        doc.errors.shouldBeNull()
        val issuerSigned = doc.issuerSigned
        val issuerAuth = issuerSigned.issuerAuth
        verifierCoseService.verifyCose(issuerAuth, issuerKey).getOrThrow().shouldBe(true)
        val issuerAuthPayload = issuerAuth.payload
        issuerAuthPayload.shouldNotBeNull()
        val mso = issuerSigned.getIssuerAuthPayloadAsMso()
        mso.shouldNotBeNull()
        mso.docType shouldBe MobileDrivingLicenceScheme.isoDocType
        val mdlItems = mso.valueDigests[MobileDrivingLicenceScheme.isoNamespace]
        mdlItems.shouldNotBeNull()

        val walletKey = mso.deviceKeyInfo.deviceKey
        val deviceSignature = doc.deviceSigned.deviceAuth.deviceSignature
        deviceSignature.shouldNotBeNull()
        verifierCoseService.verifyCose(deviceSignature, walletKey).getOrThrow().shouldBe(true)
        val namespaces = issuerSigned.namespaces
        namespaces.shouldNotBeNull()
        val issuerSignedItems = namespaces[MobileDrivingLicenceScheme.isoNamespace]
        issuerSignedItems.shouldNotBeNull()

        extractAndVerifyData(issuerSignedItems, mdlItems, FAMILY_NAME)
        extractAndVerifyData(issuerSignedItems, mdlItems, GIVEN_NAME)
    }

    private fun extractAndVerifyData(
        issuerSignedItems: IssuerSignedList,
        mdlItems: ValueDigestList,
        key: String
    ) {
        val issuerSignedItem = issuerSignedItems.entries.first { it.value.elementIdentifier == key }
        val elementValue = issuerSignedItem.value.elementValue.toString()
        elementValue.shouldNotBeNull()
        val issuerHash = mdlItems.entries.first { it.key == issuerSignedItem.value.digestId }
        issuerHash.shouldNotBeNull()
        val verifierHash = issuerSignedItem.serialized.sha256()
        verifierHash.encodeToString(Base16(strict = true)) shouldBe issuerHash.value.encodeToString(Base16(strict = true))
        println("Verifier got $key with value $elementValue and correct hash ${verifierHash.encodeToString(Base16(strict = true))}")
    }
}

private fun extractDataString(
    mdlItems: IssuerSignedList,
    key: String
): String {
    val element = mdlItems.entries.first { it.value.elementIdentifier == key }
    val value = element.value.elementValue.toString()
    value.shouldNotBeNull()
    return value
}

private fun extractDataDrivingPrivileges(
    mdlItems: IssuerSignedList,
    key: String
): Array<DrivingPrivilege> {
    val element = mdlItems.entries.first { it.value.elementIdentifier == key }
    val value = element.value.elementValue as Array<DrivingPrivilege>
    value.shouldNotBeNull()
    return value
}


fun buildIssuerSignedItem(elementIdentifier: String, elementValue: Any, digestId: UInt) = IssuerSignedItem(
    digestId = digestId,
    random = Random.nextBytes(16),
    elementIdentifier = elementIdentifier,
    elementValue = elementValue
)
