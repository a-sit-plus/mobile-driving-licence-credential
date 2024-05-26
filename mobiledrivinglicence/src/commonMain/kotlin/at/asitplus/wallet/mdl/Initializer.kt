package at.asitplus.wallet.mdl

import at.asitplus.wallet.lib.LibraryInitializer
import at.asitplus.wallet.lib.data.CredentialSubject
import at.asitplus.wallet.lib.data.jsonSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

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
            data = LibraryInitializer.ExtensionLibraryInfo(
                credentialScheme = MobileDrivingLicenceScheme,
                serializersModule = SerializersModule {
                    polymorphic(CredentialSubject::class) {
                    }
                },
            ),
            itemValueLookup = itemValueLookup(),
            itemValueEncoder = itemValueEncoder(),
            itemValueDecoder = itemValueDecoder(),
            jsonValueEncoder = jsonValueEncoder()
        )
    }

    private fun itemValueLookup(): (element: Any) -> KSerializer<*>? = {
        when (it) {
            is Array<*> -> ArraySerializer(DrivingPrivilege.serializer())
            else -> null
        }
    }

    private fun itemValueEncoder() =
        { descriptor: SerialDescriptor, index: Int, compositeEncoder: CompositeEncoder, value: Any ->
            if (value is Array<*>) {
                true.also {
                    @Suppress("UNCHECKED_CAST")
                    compositeEncoder.encodeSerializableElement(
                        descriptor,
                        index,
                        ArraySerializer(DrivingPrivilege.serializer()),
                        value as Array<DrivingPrivilege>
                    )
                }
            } else {
                false
            }
        }

    private fun itemValueDecoder() = { descriptor: SerialDescriptor, index: Int, compositeDecoder: CompositeDecoder ->
        compositeDecoder.decodeSerializableElement(
            descriptor,
            index,
            ArraySerializer(DrivingPrivilege.serializer())
        )
    }

    private fun jsonValueEncoder(): (value: Any) -> JsonElement? = {
        if (it is DrivingPrivilege) jsonSerializer.encodeToJsonElement(it) else null
    }

}
