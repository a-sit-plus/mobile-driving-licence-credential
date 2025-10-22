import at.asitplus.wallet.mdl.Initializer
import de.infix.testBalloon.framework.TestSession

class ModuleTestSession : TestSession() {
    init {
        Initializer.initWithVCK()
    }
}