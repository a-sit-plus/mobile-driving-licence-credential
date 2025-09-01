import at.asitplus.wallet.mdl.Initializer
import de.infix.testBalloon.framework.core.TestSession

class ModuleTestSession : TestSession() {
    init {
        Initializer.initWithVCK()
    }
}