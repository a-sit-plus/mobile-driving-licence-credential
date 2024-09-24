package at.asitplus.wallet.mdl


object MobileDrivingLicenceDataElements {

    /** Last name, surname, or primary identifier of the mDL holder. */
    const val FAMILY_NAME = "family_name"

    /** First name(s), other name(s), or secondary identifier, of the mDL holder. */
    const val GIVEN_NAME = "given_name"

    /** Day, month and year on which the mDL holder was born. If unknown, approximate date of birth. */
    const val BIRTH_DATE = "birth_date"

    /** Date when mDL was issued. */
    const val ISSUE_DATE = "issue_date"

    /** Date when mDL expires. */
    const val EXPIRY_DATE = "expiry_date"

    /** Alpha-2 country code, as defined in ISO 3166-1, of the issuing authority's country or territory. */
    const val ISSUING_COUNTRY = "issuing_country"

    /** Issuing authority name. */
    const val ISSUING_AUTHORITY = "issuing_authority"

    /** The number assigned or calculated by the issuing authority. */
    const val DOCUMENT_NUMBER = "document_number"

    /** A reproduction of the mDL holder's portrait. */
    const val PORTRAIT = "portrait"

    /** Driving privileges of the mDL holder. */
    const val DRIVING_PRIVILEGES = "driving_privileges"

    /** Distinguishing sign of the issuing country according to ISO/IEC 18013-1:2018, Annex F.  */
    const val UN_DISTINGUISHING_SIGN = "un_distinguishing_sign"

    /** An audit control number assigned by the issuing authority. */
    const val ADMINISTRATIVE_NUMBER = "administrative_number"

    /** mDL holder's sex using values as defined in ISO/IEC 5218. */
    const val SEX = "sex"

    /** mDL holder's height in centimetres. */
    const val HEIGHT = "height"

    /** mDL holder's weight in kilograms */
    const val WEIGHT = "weight"

    /** mDL holder's eye colour. The value shall be one of the following: "black", "blue", "brown",
     * "dichromatic", "grey", "green", "hazel", "maroon", "pink", "unknown". */
    const val EYE_COLOUR = "eye_colour"

    /** mDL holder's hair color. The value shall be one of the following: "bald", "black", "blond",
     *  "brown", "grey", "red", "auburn", "sandy","white", "unknown"  */
    const val HAIR_COLOUR = "hair_colour"

    /** Country and municipality or state/province where the mDL holder was born. */
    const val BIRTH_PLACE = "birth_place"

    /** The place where the mDL holder resides and/or may be contracted. */
    const val RESIDENT_ADDRESS = "resident_address"

    /** Date when portrait was taken. */
    const val PORTRAIT_CAPTURE_DATE = "portrait_capture_date"

    /** The age of the mDL holder. */
    const val AGE_IN_YEARS = "age_in_years"

    /** The year when the mDL holder was born. */
    const val AGE_BIRTH_YEAR = "age_birth_year"

    /** Age attestation: Over 18 years old? */
    const val AGE_OVER_18 = "age_over_18"

    /** Country subdivision code of the jurisdiction that issued the mDL as defined in ISO 3166-2:2020, Clause 8. */
    const val ISSUING_JURISDICTION = "issuing_jurisdiction"

    /** Nationality of the mDL holder as a two letter country code (alpha-2 code) defined in ISO 3166-1. */
    const val NATIONALITY = "nationality"

    /** The city where the mDL holder lives. */
    const val RESIDENT_CITY = "resident_city"

    /** The state/province/district where the mDL holder lives. */
    const val RESIDENT_STATE = "resident_state"

    /** The postal code of the mDL holder. */
    const val RESIDENT_POSTAL_CODE = "resident_postal_code"

    /** The country where the mDL holder lives as a two letter country code (alpha-2 code) defined in ISO 3166-1. */
    const val RESIDENT_COUNTRY = "resident_country"

    /** The family name of the mDL holder using full UTF-8 character set. */
    const val FAMILY_NAME_NATIONAL_CHARACTER = "family_name_national_character"

    /** The given name of the mDL holder using full UTF-8 character set. */
    const val GIVEN_NAME_NATIONAL_CHARACTER = "given_name_national_character"

    /** Image of the signature or usual mark of the mDL holder. */
    const val SIGNATURE_USUAL_MARK = "signature_usual_mark"

    val ALL_ELEMENTS = listOf(
        FAMILY_NAME,
        GIVEN_NAME,
        BIRTH_DATE,
        ISSUE_DATE,
        EXPIRY_DATE,
        ISSUING_COUNTRY,
        ISSUING_AUTHORITY,
        DOCUMENT_NUMBER,
        PORTRAIT,
        DRIVING_PRIVILEGES,
        UN_DISTINGUISHING_SIGN,
        ADMINISTRATIVE_NUMBER,
        SEX,
        HEIGHT,
        WEIGHT,
        EYE_COLOUR,
        HAIR_COLOUR,
        BIRTH_PLACE,
        RESIDENT_ADDRESS,
        PORTRAIT_CAPTURE_DATE,
        AGE_IN_YEARS,
        AGE_BIRTH_YEAR,
        AGE_OVER_18,
        ISSUING_JURISDICTION,
        NATIONALITY,
        RESIDENT_CITY,
        RESIDENT_STATE,
        RESIDENT_POSTAL_CODE,
        RESIDENT_COUNTRY,
        FAMILY_NAME_NATIONAL_CHARACTER,
        GIVEN_NAME_NATIONAL_CHARACTER,
        SIGNATURE_USUAL_MARK,
    )

    val MANDATORY_ELEMENTS = listOf(
        FAMILY_NAME,
        GIVEN_NAME,
        BIRTH_DATE,
        ISSUE_DATE,
        EXPIRY_DATE,
        ISSUING_COUNTRY,
        ISSUING_AUTHORITY,
        DOCUMENT_NUMBER,
        PORTRAIT,
        DRIVING_PRIVILEGES,
        UN_DISTINGUISHING_SIGN,
    )
}
