package inc.kaizen.service.sprout.processor.provider

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import inc.kaizen.service.sprout.processor.APIAnnotationProcessor

class APIAnnotationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return APIAnnotationProcessor(environment)
    }
}