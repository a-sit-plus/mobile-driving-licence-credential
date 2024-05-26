package at.asitplus.wallet.mdl

import io.kotest.core.config.AbstractProjectConfig

object KotestConfig : AbstractProjectConfig() {
    init {
        Initializer.initWithVcLib()
    }
}