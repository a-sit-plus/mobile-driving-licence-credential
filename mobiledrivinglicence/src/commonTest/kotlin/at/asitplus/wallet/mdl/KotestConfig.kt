package at.asitplus.wallet.mdl

import io.kotest.core.config.AbstractProjectConfig

class KotestConfig : AbstractProjectConfig() {
    init {
        Initializer.initWithVck()
    }
}