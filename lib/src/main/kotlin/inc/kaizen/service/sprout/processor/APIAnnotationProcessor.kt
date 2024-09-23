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
import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.impl.*
import java.util.*

class APIAnnotationProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(API::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { element ->
            environment.logger.info("Processing ${element.simpleName.asString()}")
            println("Processing ${element.simpleName.asString()}")
            processAPIAnnotation(element)
        }
        return emptyList()
    }

    private fun processAPIAnnotation(element: KSClassDeclaration) {
//        val generators = ServiceLoader.load(IClassContentGenerator::class.java).toList()
        val generators = listOf(
            ControllerClassGenerator(),
            ServiceClassGenerator(),
            RepositoryClassGenerator(),
            ExtensionClassGenerator(),
            ConverterClassGenerator(),
            EntityServiceClassGenerator(),
            EntityClassGenerator()
        )

        element.annotations.find { it.shortName.asString() == "API" }.let { annotation ->
            val apiAnnotation = annotation.nonNullify()
            environment.logger.info("Processing API annotation: ${apiAnnotation.shortName.asString()}")

            val basePackageName = getPackageName(apiAnnotation)
            val serviceName = getServiceName(apiAnnotation)

            val extensions = mapOf(
                "basePackageName" to basePackageName,
                "serviceName" to serviceName
            )

            generators.forEach { generator ->
                println("Generating ${generator::class.simpleName} for $serviceName")
                environment.logger.info("Generating ${generator::class.simpleName} for $serviceName")
                generator.generate(environment.codeGenerator, extensions)
            }
        }
    }

    private fun getPackageName(apiAnnotation: KSAnnotation) = getArgumentValue(apiAnnotation, "basePackageName")
    private fun getServiceName(apiAnnotation: KSAnnotation) = getArgumentValue(apiAnnotation, "serviceName")

    private fun getArgumentValue(apiAnnotation: KSAnnotation, argumentName: String) = apiAnnotation
        .findArgumentByName(argumentName)
        .get { it.value as String }
        .nonNullify()
}