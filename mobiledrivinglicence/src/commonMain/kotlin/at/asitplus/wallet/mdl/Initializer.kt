package at.asitplus.wallet.mdl

import at.asitplus.wallet.lib.ItemValueDecoder
import at.asitplus.wallet.lib.ItemValueEncoder
import at.asitplus.wallet.lib.JsonValueEncoder
import at.asitplus.wallet.lib.LibraryInitializer
import at.asitplus.wallet.lib.SerializerLookup
import at.asitplus.wallet.lib.data.jsonSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.json.encodeToJsonElement

object Initializer {

    /**
     * A reference to this class is enough to trigger the init block
     */
    init {
        initWithVcLib()
    }

    /**
     * This has to be called first, before anything first, to load the
     * relevant classes of this library into the base implementations of vclib
     */
    fun initWithVcLib() {
        LibraryInitializer.registerExtensionLibrary(
            credentialScheme = MobileDrivingLicenceScheme,
            serializerLookup = serializerLookup(),
            itemValueEncoder = itemValueEncoder(),
            itemValueDecoder = itemValueDecoder(),
            jsonValueEncoder = jsonValueEncoder()
        )
    }

    private fun serializerLookup(): SerializerLookup = {
        if (it is Array<*>) ArraySerializer(DrivingPrivilege.serializer()) else null
    }

    private fun itemValueEncoder(): ItemValueEncoder = { descriptor, index, compositeEncoder, value ->
        if (value is Array<*> && value.isNotEmpty() && value.all { it is DrivingPrivilege }) {
            true.also {
                encodeArrayOfDrivingPrivileges(compositeEncoder, descriptor, index, value)
            }
        } else {
            false
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun encodeArrayOfDrivingPrivileges(
        compositeEncoder: CompositeEncoder,
        descriptor: SerialDescriptor,
        index: Int,
        value: Any
    ) {
        compositeEncoder.encodeSerializableElement(
            descriptor,
            index,
            ArraySerializer(DrivingPrivilege.serializer()),
            value as Array<DrivingPrivilege>
        )
    }

    private fun itemValueDecoder(): ItemValueDecoder = { descriptor, index, compositeDecoder ->
        compositeDecoder.decodeSerializableElement(
            descriptor,
            index,
            ArraySerializer(DrivingPrivilege.serializer())
        )
    }

    private fun jsonValueEncoder(): JsonValueEncoder = {
        if (it is DrivingPrivilege) jsonSerializer.encodeToJsonElement(it) else null
    }

}
