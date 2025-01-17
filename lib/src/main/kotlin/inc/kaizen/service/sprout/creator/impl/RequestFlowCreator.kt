package inc.kaizen.service.sprout.creator.impl

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.creator.IRequestFlowCreator
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.plural
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import inc.kaizen.service.sprout.hooks.file.impl.*
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import inc.kaizen.service.sprout.hooks.method.impl.*

class RequestFlowCreator: IRequestFlowCreator {

    private val methodHooks: MutableMap<MethodRequest, Map<Component, IMethodHook>> = mutableMapOf()
    private val classHooks: MutableMap<Component, Map<IClassHook, Boolean>> = mutableMapOf()
    private val componentToTypeMapping: MutableMap<Component, MutableMap<TypeSpec.Builder, Boolean>> = mutableMapOf()

    init {
        classHooks[Component.CONTROLLER] = mapOf(ControllerClassHook() to true)
        classHooks[Component.SERVICE] = mapOf(ServiceClassHook() to true, EntityServiceClassHook() to false)
        classHooks[Component.REPOSITORY] = mapOf(RepositoryClassHook() to true)
        classHooks[Component.ENTITY] = mapOf(EntityClassHook() to false)
        classHooks[Component.CONVERTER] = mapOf(ModelConverterClassHook() to false, EntityConverterClassHook() to false)


        val controllerMethodHooks = mutableMapOf<Component, IMethodHook>()
        controllerMethodHooks[Component.CONTROLLER] = ControllerGetMethodHook()
        controllerMethodHooks[Component.SERVICE] = ServiceGetMethodHook()
        methodHooks[MethodRequest.GET] = controllerMethodHooks

        val controllerPostMethodHooks = mutableMapOf<Component, IMethodHook>()
        controllerPostMethodHooks[Component.CONTROLLER] = ControllerPostMethodHook()
        controllerPostMethodHooks[Component.SERVICE] = ServicePostMethodHook()
        methodHooks[MethodRequest.POST] = controllerPostMethodHooks

        val controllerPutMethodHooks = mutableMapOf<Component, IMethodHook>()
        controllerPutMethodHooks[Component.CONTROLLER] = ControllerPutMethodHook()
        controllerPutMethodHooks[Component.SERVICE] = ServicePutMethodHook()
        methodHooks[MethodRequest.PUT] = controllerPutMethodHooks

        val controllerDeleteMethodHooks = mutableMapOf<Component, IMethodHook>()
        controllerDeleteMethodHooks[Component.CONTROLLER] = ControllerDeleteMethodHook()
        controllerDeleteMethodHooks[Component.SERVICE] = ServiceDeleteMethodHook()
        methodHooks[MethodRequest.DELETE] = controllerDeleteMethodHooks

        val controllerGetManyMethodHooks = mutableMapOf<Component, IMethodHook>()
        controllerGetManyMethodHooks[Component.CONTROLLER] = ControllerGetManyMethodHook()
        controllerGetManyMethodHooks[Component.SERVICE] = ServiceGetManyMethodHook()
        methodHooks[MethodRequest.GET_ALL] = controllerGetManyMethodHooks
    }

    override fun flow(environment: SymbolProcessorEnvironment, extensions: Map<String, Any>) {
        val tempExtensions = extensions.toMutableMap()
        val basePackageName = extensions[BASE_PACKAGE_NAME] as String
        val serviceName = extensions[SERVICE_NAME] as String
        tempExtensions[SERVICE_NAME_PLURAL] = serviceName.plural()

        val components = Component.values()
        components.forEach { component ->
            val componentName = component.name.lowercase().toCamelCase()
            val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${componentName.capitalizeFirstLetter()}"
            val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
            val filePath = "${packageName.replace('.', '/')}/$className"

            tempExtensions[CLASS_NAME] = className
            tempExtensions[PACKAGE_NAME] = packageName
            tempExtensions[FILE_PATH] = filePath

            initialize(component, tempExtensions)
        }

        val methodRequests = arrayOf(
            MethodRequest.GET,
            MethodRequest.POST,
            MethodRequest.PUT,
            MethodRequest.DELETE,
            MethodRequest.GET_ALL
        )

        methodRequests.forEach { methodRequest ->
            components.forEach { component ->
                val classSpecs = componentToTypeMapping[component]
                if (classSpecs != null) {
                    classSpecs.forEach { classSpec, callHooks ->
                        val componentName = component.name.lowercase().toCamelCase()
                        val className = "${
                            serviceName.toCamelCase().capitalizeFirstLetter()
                        }${componentName.capitalizeFirstLetter()}"
                        val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
                        val filePath = "${packageName.replace('.', '/')}/$className"

                        tempExtensions[CLASS_NAME] = className
                        tempExtensions[PACKAGE_NAME] = packageName
                        tempExtensions[FILE_PATH] = filePath

                        if (callHooks) {
                            val function = flow(component, methodRequest, tempExtensions)
                            if (function != null) {
                                classSpec.addFunction(function.build())
                            }
                        }
                    }
                }
            }
        }

        generateFiles(environment, extensions)
    }

    fun generateFiles(environment: SymbolProcessorEnvironment, extensions: Map<String, Any>) {
        val components = Component.values()
        components.forEach { component ->
            val basePackageName = extensions[BASE_PACKAGE_NAME] as String
            val serviceName = extensions[SERVICE_NAME] as String

            val componentName = component.name.lowercase().toCamelCase()
            val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"

            val typeSpecs = componentToTypeMapping[component]
            if (typeSpecs == null)
                throw Exception("Class is missing for component: ${component}")

            typeSpecs.forEach { typeSpecBuilder, _ ->
                val typeSpec = typeSpecBuilder.build()
                val className = typeSpec.name

                if(!environment.codeGenerator.generatedFile.map { it.nameWithoutExtension }.contains(className)) { //FIXME this is a hack to avoid generating the same file multiple times
                    val filePath = "${packageName.replace('.', '/')}/$className"
                    val fileSpec = FileSpec
                        .builder(packageName, className!!)
                        .addType(typeSpec)
                        .build()

                    environment.logger.info("Generating file: $filePath")
                    val file = environment.codeGenerator.createNewFileByPath(
                        Dependencies.ALL_FILES,
                        filePath
                    )

                    val content = fileSpec.toString().toByteArray()
                    file.write(content)
                    file.close()
                }
            }
        }
    }

    fun initialize(component: Component, extensions: Map<String, Any>) {
        val hooks = findClassHook(component)
        componentToTypeMapping[component] = componentToTypeMapping[component] ?: mutableMapOf()

        val tempExtensions = extensions.toMutableMap()
        val basePackageName = extensions[BASE_PACKAGE_NAME] as String
        val serviceName = extensions[SERVICE_NAME] as String
        tempExtensions[SERVICE_NAME_PLURAL] = serviceName.plural()

        hooks.forEach { hook, callHooks ->
            val componentName = component.name.lowercase().toCamelCase()
            val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${componentName.capitalizeFirstLetter()}"
            val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
            val filePath = "${packageName.replace('.', '/')}/$className"

            tempExtensions[CLASS_NAME] = className
            tempExtensions[PACKAGE_NAME] = packageName
            tempExtensions[FILE_PATH] = filePath

            componentToTypeMapping[component]?.put(hook.hook(component, tempExtensions), callHooks)
        }
    }

    fun flow(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder? {
        val methodHook = findMethodHook(component, methodRequest)
        return methodHook?.hook(component, methodRequest, extensions)
    }

    fun findClassHook(component: Component): Map<IClassHook, Boolean> {
        val hooks = classHooks[component]
        if (hooks == null)
            throw Exception("Class hook is missing for component: ${component}")

        return hooks
    }

    fun findMethodHook(component: Component, methodRequest: MethodRequest, validate: Boolean = false): IMethodHook? {
        val requestHooks = methodHooks[methodRequest]
        if(validate)
            if (requestHooks == null)
                throw Exception("Hook configuration is missing for method: ${methodRequest}")
            else if (requestHooks[component] == null)
                throw Exception("Component hook is missing for method: ${methodRequest} and component: ${component}")
            else
                return requestHooks[component]
        else
            return requestHooks?.get(component)
    }
}