package inc.kaizen.service.sprout.processor

import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Model
import inc.kaizen.service.sprout.extension.findArgumentByName
import inc.kaizen.service.sprout.extension.nonNullify
import inc.kaizen.service.sprout.generator.IClassContentGenerator
import inc.kaizen.service.sprout.generator.MODEL_PACKAGE_NAME
import inc.kaizen.service.sprout.generator.impl.*
import java.util.*
import kotlin.collections.set
import kotlin.reflect.KClass

class APIAnnotationProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val models = resolver.getSymbolsWithAnnotation(Model::class.qualifiedName!!)
        models.filterIsInstance<KSClassDeclaration>().forEach { element ->
            environment.logger.info("Processing ${element.simpleName.asString()}")
            println("Processing ${element.simpleName.asString()}")
            processModelAnnotation(element, resolver)
        }

        val symbols = resolver.getSymbolsWithAnnotation(API::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { element ->
            environment.logger.info("Processing ${element.simpleName.asString()}")
            println("Processing ${element.simpleName.asString()}")
            processAPIAnnotation(element, resolver)
        }

        return emptyList()
    }

    private fun processAPIAnnotation(element: KSClassDeclaration, resolver: Resolver) {
//        val generators = ServiceLoader.load(IClassContentGenerator::class.java).toList()
        val generators = listOf(
            ControllerClassGenerator(),
        )

        element.annotations.find { it.shortName.asString() == API::class.simpleName }.let { annotation ->
            val apiAnnotation = annotation.nonNullify()
            environment.logger.info("Processing API annotation: ${apiAnnotation.shortName.asString()}")

            val modelArgument = apiAnnotation.arguments.find { it.name?.asString() == "model" }
            val modelClass = modelArgument?.value as? KClass<*>

            if (modelClass != null) {
                val modelClassDeclaration = resolver.getClassDeclarationByName(modelClass.qualifiedName!!)
                val hasModelAnnotation = modelClassDeclaration?.annotations?.any { it.shortName.asString() == Model::class.simpleName } == true

                if (!hasModelAnnotation) {
                    environment.logger.error("The class ${modelClass.qualifiedName} must be annotated with @Model")
                    throw IllegalArgumentException("The class ${modelClass.qualifiedName} must be annotated with @Model")
                }
            }

            val extensions = apiAnnotation
                .arguments
                .associate { it.name?.asString() to it.value }
                .filterValues { it != null }
                .mapValues { it.value!! }
                .mapKeys { it.key!! }
                .toMutableMap()

            extensions[MODEL_PACKAGE_NAME] = (apiAnnotation.findArgumentByName("model")?.value as? KSType)?.declaration?.packageName?.asString() ?: ""

            generators.forEach { generator ->
                environment.logger.info("Generating ${generator::class.simpleName}")
                generator.generate(environment.codeGenerator, extensions)
            }
        }
    }

    private fun processModelAnnotation(element: KSClassDeclaration, resolver: Resolver) {
        val generators = listOf(
            BaseEntityClassGenerator(),
            BaseModelClassGenerator(),
            ServiceInterfaceGenerator(),
            ControllerInterfaceGenerator(),
            ServiceClassGenerator(),
            RepositoryClassGenerator(),
            ExtensionClassGenerator(),
            ModelConverterClassGenerator(),
            EntityConverterClassGenerator(),
            EntityServiceClassGenerator(),
            EntityClassGenerator()
        )

        element.annotations.find { it.shortName.asString() == Model::class.simpleName }.let { annotation ->
            val modelAnnotation = annotation.nonNullify()
            environment.logger.info("Processing Model annotation: ${modelAnnotation.shortName.asString()}")

            val extensions = modelAnnotation
                .arguments
                .associate { it.name?.asString() to it.value }
                .filterValues { it != null }
                .mapValues { it.value!! }
                .mapKeys { it.key!! }
                .toMutableMap()

            extensions["model"] = element.asStarProjectedType().declaration
            extensions[MODEL_PACKAGE_NAME] = element.packageName.asString()

            generators.forEach { generator ->
                environment.logger.info("Generating ${generator::class.simpleName}")
                generator.generate(environment.codeGenerator, extensions)
            }
        }
    }
}

fun main(args: Array<String>) {
    val generators = ServiceLoader.load(IClassContentGenerator::class.java).toList()
    generators.forEach { println(it) }
}