package inc.kaizen.service.sprout.processor

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getClassDeclarationByName
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import inc.kaizen.service.sprout.annotation.API
import inc.kaizen.service.sprout.annotation.Model
import inc.kaizen.service.sprout.annotation.Request
import inc.kaizen.service.sprout.base.extension.nonNullify
import inc.kaizen.service.sprout.creator.impl.RequestFlowCreator
import inc.kaizen.service.sprout.extension.findArgumentByName
import inc.kaizen.service.sprout.constant.EXTENSION_METHODS
import inc.kaizen.service.sprout.constant.MODEL_PACKAGE_NAME
import kotlin.collections.set
import kotlin.reflect.KClass

class SproutAnnotationProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        environment.logger.info("Module: ${resolver.getModuleName().asString()}")
        val symbols = resolver.getSymbolsWithAnnotation(API::class.qualifiedName!!)
        symbols.filterIsInstance<KSClassDeclaration>().forEach { element ->
            environment.logger.info("Processing ${element.simpleName.asString()}")
            println("Processing ${element.simpleName.asString()}")
            processAPIAnnotation(element, resolver)
        }
        return emptyList()
    }

    private fun processAPIAnnotation(element: KSClassDeclaration, resolver: Resolver) {
        val requestFlowCreator = RequestFlowCreator()
        element.annotations.find { it.shortName.asString() == API::class.simpleName }.let { annotation ->
            val apiAnnotation = annotation.nonNullify()
            environment.logger.info("Processing API annotation: ${element.simpleName.asString()}")

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

            val extensions = mutableMapOf<String, Any>()

            val apiExtensions = apiAnnotation
                .arguments
                .associate { it.name?.asString() to it.value }
                .filterValues { it != null }
                .mapValues { it.value!! }
                .mapKeys { it.key!! }
                .toMutableMap()
            extensions.putAll(apiExtensions)

            val model = apiAnnotation.findArgumentByName("model")?.value as? KSType
            extensions["model"] = model?.starProjection()?.declaration as KSClassDeclaration
            model
                .declaration
                .annotations
                .find { it.shortName.asString() == Model::class.simpleName }
                ?.let {
                    val modelExtensions = it
                        .arguments
                        .associate { it.name?.asString() to it.value }
                        .filterValues { it != null }
                        .mapValues { it.value!! }
                        .mapKeys { it.key!! }
                        .toMutableMap()
                    extensions.putAll(modelExtensions)
                }

            extensions[MODEL_PACKAGE_NAME] = model.declaration.packageName.asString()
            extensions[EXTENSION_METHODS] = element
                .getAllFunctions()
                .filter { function -> function.annotations.any { it.shortName.asString() == Request::class.simpleName } }
                .toList()

            requestFlowCreator.flow(environment, extensions)
        }
    }
}