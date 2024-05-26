package at.asitplus.wallet.mdl

import at.asitplus.wallet.lib.data.ConstantIndex


/**
 * Mobile Driving Licence scheme, according to ISO/IEC 18013-5:2021 standard: Data structure for mDL (7.2.1)
 */
object MobileDrivingLicenceScheme : ConstantIndex.CredentialScheme {

    override val schemaUri: String = "https://wallet.a-sit.at/schemas/1.0.0/MobileDrivingLicence2023.json"
    override val vcType: String = "MobileDrivingLicence"
    override val isoNamespace: String = "org.iso.18013.5.1"
    override val isoDocType: String = "org.iso.18013.5.1.mDL"
    override val claimNames: Collection<String> = MobileDrivingLicenceDataElements.ALL_ELEMENTS.toList()
}
