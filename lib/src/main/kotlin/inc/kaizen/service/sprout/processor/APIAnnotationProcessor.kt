package inc.kaizen.service.sprout.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.extension.findArgumentByName
import inc.kaizen.service.sprout.extension.get
import inc.kaizen.service.sprout.extension.nonNullify
import inc.kaizen.service.sprout.generator.impl.ControllerClassGenerator
import inc.kaizen.service.sprout.generator.impl.RepositoryClassGenerator
import inc.kaizen.service.sprout.generator.impl.ServiceClassGenerator

class APIAnnotationProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(API::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { element ->
            processAPIAnnotation(element)
        }
        return emptyList()
    }

    private fun processAPIAnnotation(element: KSClassDeclaration) {
        val generators = listOf(ControllerClassGenerator(), ServiceClassGenerator(), RepositoryClassGenerator())

        element.annotations.find { it.shortName.asString() == "API" }.let { annotation ->
            val apiAnnotation = annotation.nonNullify()
            environment.logger.info("Processing API annotation: ${apiAnnotation.shortName.asString()}")

            val packageName = getPackageName(apiAnnotation)
            val serviceName = getServiceName(apiAnnotation)

            generators.forEach { generator ->
                generator.generate(environment.codeGenerator, packageName, serviceName)
            }
        }
    }

    private fun getPackageName(apiAnnotation: KSAnnotation) = getArgumentValue(apiAnnotation, "packageName")
    private fun getServiceName(apiAnnotation: KSAnnotation) = getArgumentValue(apiAnnotation, "serviceName")

    private fun getArgumentValue(apiAnnotation: KSAnnotation, argumentName: String) = apiAnnotation
        .findArgumentByName(argumentName)
        .get { it.value as String }
        .nonNullify()
}