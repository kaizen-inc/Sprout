package inc.kaizen.service.sprout.processor.provider

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import inc.kaizen.service.sprout.processor.SproutAnnotationProcessor

class APIAnnotationProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        environment.logger.logging("Creating APIAnnotationProcessor")
        environment.platforms.forEach { platform ->
            environment.logger.info("platform: ${platform.platformName}")
        }
        return SproutAnnotationProcessor(environment)
    }
}