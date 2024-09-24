package at.asitplus.wallet.mdl

import at.asitplus.KmmResult.Companion.wrap
import at.asitplus.signum.indispensable.io.ByteArrayBase64UrlSerializer
import at.asitplus.wallet.lib.iso.vckCborSerializer
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
import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.ByteString
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray


/**
 * Part of the ISO/IEC 18013-5:2021 standard: Data structure for mDL (7.2.1)
 */
@Serializable
data class MobileDrivingLicence(
    /** Last name, surname, or primary identifier of the mDL holder. */
    @SerialName(FAMILY_NAME)
    val familyName: String,

    /** First name(s), other name(s), or secondary identifier, of the mDL holder. */
    @SerialName(GIVEN_NAME)
    val givenName: String,

    /** Day, month and year on which the mDL holder was born. If unknown, approximate date of birth. */
    @SerialName(BIRTH_DATE)
    val dateOfBirth: LocalDate? = null,

    /** Date when mDL was issued. */
    @SerialName(ISSUE_DATE)
    val issueDate: LocalDate,

    /** Date when mDL expires. */
    @SerialName(EXPIRY_DATE)
    val expiryDate: LocalDate,

    /** Alpha-2 country code, as defined in ISO 3166-1, of the issuing authority's country or territory. */
    @SerialName(ISSUING_COUNTRY)
    val issuingCountry: String? = null,

    /** Issuing authority name. */
    @SerialName(ISSUING_AUTHORITY)
    val issuingAuthority: String? = null,

    /** The number assigned or calculated by the issuing authority. */
    @SerialName(DOCUMENT_NUMBER)
    val licenceNumber: String,

    /** A reproduction of the mDL holder's portrait. */
    @SerialName(PORTRAIT)
    @ByteString
    @Serializable(with = ByteArrayBase64UrlSerializer::class)
    val portrait: ByteArray,

    /** Driving privileges of the mDL holder. */
    @SerialName(DRIVING_PRIVILEGES)
    val drivingPrivileges: List<DrivingPrivilege>,

    /** Distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F.  */
    @SerialName(UN_DISTINGUISHING_SIGN)
    val unDistinguishingSign: String? = null,

    /** An audit control number assigned by the issuing authority. */
    @SerialName(ADMINISTRATIVE_NUMBER)
    val administrativeNumber: String? = null,

    /** mDL holder's sex using values as defined in ISO/IEC 5218. */
    @SerialName(SEX)
    @Serializable(with = IsoSexEnumSerializer::class)
    val sex: IsoSexEnum? = null,

    /** mDL holder's height in centimetres. */
    @SerialName(HEIGHT)
    val height: UInt? = null,

    /** mDL holder's weight in kilograms */
    @SerialName(WEIGHT)
    val weight: UInt? = null,

    /** mDL holder's eye colour. The value shall be one of the following: "black", "blue", "brown",
     * "dichromatic", "grey", "green", "hazel", "maroon", "pink", "unknown". */
    @SerialName(EYE_COLOUR)
    val eyeColor: String? = null,

    /** mDL holder's hair color. The value shall be one of the following: "bald", "black", "blond",
     *  "brown", "grey", "red", "auburn", "sandy","white", "unknown"  */
    @SerialName(HAIR_COLOUR)
    val hairColor: String? = null,

    /** Country and municipality or state/province where the mDL holder was born. */
    @SerialName(BIRTH_PLACE)
    val placeOfBirth: String? = null,

    /** The place where the mDL holder resides and/or may be contracted. */
    @SerialName(RESIDENT_ADDRESS)
    val placeOfResidence: String? = null,

    /** Date when portrait was taken. */
    @SerialName(PORTRAIT_CAPTURE_DATE)
    val portraitImageTimestamp: LocalDate? = null,

    /** The age of the mDL holder. */
    @SerialName(AGE_IN_YEARS)
    val ageInYears: UInt? = null,

    /** The year when the mDL holder was born. */
    @SerialName(AGE_BIRTH_YEAR)
    val ageBirthYear: UInt? = null,

    /** Age attestation: Over 18 years old? */
    @SerialName(AGE_OVER_18)
    val ageOver18: Boolean? = null,

    /** Country subdivision code of the jurisdiction that issued the mDL as defined in ISO 3166-2:2020, Clause 8. */
    @SerialName(ISSUING_JURISDICTION)
    val issuingJurisdiction: String? = null,

    /** Nationality of the mDL holder as a two letter country code (alpha-2 code) defined in ISO 3166-1. */
    @SerialName(NATIONALITY)
    val nationality: String? = null,

    /** The city where the mDL holder lives. */
    @SerialName(RESIDENT_CITY)
    val residentCity: String? = null,

    /** The state/province/district where the mDL holder lives. */
    @SerialName(RESIDENT_STATE)
    val residentState: String? = null,

    /** The postal code of the mDL holder. */
    @SerialName(RESIDENT_POSTAL_CODE)
    val residentPostalCode: String? = null,

    /** The country where the mDL holder lives as a two letter country code (alpha-2 code) defined in ISO 3166-1. */
    @SerialName(RESIDENT_COUNTRY)
    val residentCountry: String? = null,

    /** The family name of the mDL holder using full UTF-8 character set. */
    @SerialName(FAMILY_NAME_NATIONAL_CHARACTER)
    val familyNameNationalCharacters: String? = null,

    /** The given name of the mDL holder using full UTF-8 character set. */
    @SerialName(GIVEN_NAME_NATIONAL_CHARACTER)
    val givenNameNationalCharacters: String? = null,

    /** Image of the signature or usual mark of the mDL holder. */
    @ByteString
    @SerialName(SIGNATURE_USUAL_MARK)
    @Serializable(with = ByteArrayBase64UrlSerializer::class)
    val signatureOrUsualMark: ByteArray? = null,
) {
    fun serialize() = vckCborSerializer.encodeToByteArray(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as MobileDrivingLicence

        if (familyName != other.familyName) return false
        if (givenName != other.givenName) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (issueDate != other.issueDate) return false
        if (expiryDate != other.expiryDate) return false
        if (issuingCountry != other.issuingCountry) return false
        if (issuingAuthority != other.issuingAuthority) return false
        if (licenceNumber != other.licenceNumber) return false
        if (!portrait.contentEquals(other.portrait)) return false
        if (drivingPrivileges != other.drivingPrivileges) return false
        if (unDistinguishingSign != other.unDistinguishingSign) return false
        if (administrativeNumber != other.administrativeNumber) return false
        if (sex != other.sex) return false
        if (height != other.height) return false
        if (weight != other.weight) return false
        if (eyeColor != other.eyeColor) return false
        if (hairColor != other.hairColor) return false
        if (placeOfBirth != other.placeOfBirth) return false
        if (placeOfResidence != other.placeOfResidence) return false
        if (portraitImageTimestamp != other.portraitImageTimestamp) return false
        if (ageInYears != other.ageInYears) return false
        if (ageBirthYear != other.ageBirthYear) return false
        if (ageOver18 != other.ageOver18) return false
        if (issuingJurisdiction != other.issuingJurisdiction) return false
        if (nationality != other.nationality) return false
        if (residentCity != other.residentCity) return false
        if (residentState != other.residentState) return false
        if (residentPostalCode != other.residentPostalCode) return false
        if (residentCountry != other.residentCountry) return false
        if (familyNameNationalCharacters != other.familyNameNationalCharacters) return false
        if (givenNameNationalCharacters != other.givenNameNationalCharacters) return false
        if (signatureOrUsualMark != null) {
            if (other.signatureOrUsualMark == null) return false
            if (!signatureOrUsualMark.contentEquals(other.signatureOrUsualMark)) return false
        } else if (other.signatureOrUsualMark != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = familyName.hashCode()
        result = 31 * result + givenName.hashCode()
        result = 31 * result + (dateOfBirth?.hashCode() ?: 0)
        result = 31 * result + issueDate.hashCode()
        result = 31 * result + expiryDate.hashCode()
        result = 31 * result + (issuingCountry?.hashCode() ?: 0)
        result = 31 * result + (issuingAuthority?.hashCode() ?: 0)
        result = 31 * result + licenceNumber.hashCode()
        result = 31 * result + portrait.contentHashCode()
        result = 31 * result + drivingPrivileges.hashCode()
        result = 31 * result + (unDistinguishingSign?.hashCode() ?: 0)
        result = 31 * result + (administrativeNumber?.hashCode() ?: 0)
        result = 31 * result + (sex?.hashCode() ?: 0)
        result = 31 * result + (height?.hashCode() ?: 0)
        result = 31 * result + (weight?.hashCode() ?: 0)
        result = 31 * result + (eyeColor?.hashCode() ?: 0)
        result = 31 * result + (hairColor?.hashCode() ?: 0)
        result = 31 * result + (placeOfBirth?.hashCode() ?: 0)
        result = 31 * result + (placeOfResidence?.hashCode() ?: 0)
        result = 31 * result + (portraitImageTimestamp?.hashCode() ?: 0)
        result = 31 * result + (ageInYears?.hashCode() ?: 0)
        result = 31 * result + (ageBirthYear?.hashCode() ?: 0)
        result = 31 * result + (ageOver18?.hashCode() ?: 0)
        result = 31 * result + (issuingJurisdiction?.hashCode() ?: 0)
        result = 31 * result + (nationality?.hashCode() ?: 0)
        result = 31 * result + (residentCity?.hashCode() ?: 0)
        result = 31 * result + (residentState?.hashCode() ?: 0)
        result = 31 * result + (residentPostalCode?.hashCode() ?: 0)
        result = 31 * result + (residentCountry?.hashCode() ?: 0)
        result = 31 * result + (familyNameNationalCharacters?.hashCode() ?: 0)
        result = 31 * result + (givenNameNationalCharacters?.hashCode() ?: 0)
        result = 31 * result + (signatureOrUsualMark?.contentHashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "MobileDrivingLicence(familyName='$familyName'," +
                " givenName='$givenName'," +
                " dateOfBirth=$dateOfBirth," +
                " issueDate=$issueDate," +
                " expiryDate=$expiryDate," +
                " issuingCountry='$issuingCountry'," +
                " issuingAuthority='$issuingAuthority'," +
                " licenceNumber='$licenceNumber'," +
                " portrait=${portrait.encodeToString(Base16(strict = true))}," +
                " drivingPrivileges=${drivingPrivileges}," +
                " unDistinguishingSign='$unDistinguishingSign'," +
                " administrativeNumber=$administrativeNumber," +
                " sex=$sex," +
                " height=$height," +
                " weight=$weight," +
                " eyeColor=$eyeColor," +
                " hairColor=$hairColor," +
                " placeOfBirth=$placeOfBirth," +
                " placeOfResidence=$placeOfResidence," +
                " portraitImageTimestamp=$portraitImageTimestamp," +
                " ageInYears=$ageInYears," +
                " ageBirthYear=$ageBirthYear," +
                " ageOver18=$ageOver18," +
                " issuingJurisdiction=$issuingJurisdiction," +
                " nationality=$nationality," +
                " residentCity=$residentCity," +
                " residentState=$residentState," +
                " residentPostalCode=$residentPostalCode," +
                " residentCountry=$residentCountry," +
                " familyNameNationalCharacters=$familyNameNationalCharacters," +
                " givenNameNationalCharacters=$givenNameNationalCharacters," +
                " signatureOrUsualMark=${signatureOrUsualMark?.encodeToString(Base16(strict = true))})"
    }

    companion object {
        fun deserialize(it: ByteArray) = kotlin.runCatching {
            vckCborSerializer.decodeFromByteArray<MobileDrivingLicence>(it)
        }.wrap()
    }
}
