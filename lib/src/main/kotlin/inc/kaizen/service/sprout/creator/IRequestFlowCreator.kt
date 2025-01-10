package inc.kaizen.service.sprout.creator

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import inc.kaizen.service.sprout.annotation.MethodRequest

interface IRequestFlowCreator {
    // Create a flow that creates method
    // controller
    // service

    fun flow(environment: SymbolProcessorEnvironment, extensions: Map<String, Any>)
}