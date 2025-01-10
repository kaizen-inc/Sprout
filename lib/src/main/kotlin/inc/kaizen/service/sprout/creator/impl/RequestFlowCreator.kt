package inc.kaizen.service.sprout.creator.impl

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import inc.kaizen.service.sprout.annotation.MethodRequest
import inc.kaizen.service.sprout.creator.IRequestFlowCreator
import inc.kaizen.service.sprout.extension.capitalizeFirstLetter
import inc.kaizen.service.sprout.extension.toCamelCase
import inc.kaizen.service.sprout.generator.*
import inc.kaizen.service.sprout.hooks.Component
import inc.kaizen.service.sprout.hooks.file.IClassHook
import inc.kaizen.service.sprout.hooks.file.impl.ControllerClassHook
import inc.kaizen.service.sprout.hooks.file.impl.ControllerGetHook
import inc.kaizen.service.sprout.hooks.file.impl.RepositoryClassHook
import inc.kaizen.service.sprout.hooks.file.impl.ServiceClassHook
import inc.kaizen.service.sprout.hooks.method.IMethodHook
import inc.kaizen.service.sprout.hooks.method.impl.RepositoryGetHook
import inc.kaizen.service.sprout.hooks.method.impl.ServiceGetHook

class RequestFlowCreator: IRequestFlowCreator {

    private val methodHooks: MutableMap<MethodRequest, Map<Component, IMethodHook>> = mutableMapOf()
    private val classHooks: MutableMap<Component, IClassHook> = mutableMapOf()
    private val componentToTypeMapping: MutableMap<Component, TypeSpec.Builder> = mutableMapOf()

    init {
        classHooks[Component.CONTROLLER] = ControllerClassHook()
        classHooks[Component.SERVICE] = ServiceClassHook()
        classHooks[Component.REPOSITORY] = RepositoryClassHook()

        val controllerMethodHooks = mutableMapOf<Component, IMethodHook>()
//        controllerMethodHooks[Component.CONTROLLER] = ControllerGetHook()
//        controllerMethodHooks[Component.SERVICE] = ServiceGetHook()
//        controllerMethodHooks[Component.REPOSITORY] = RepositoryGetHook()
        methodHooks[MethodRequest.GET] = controllerMethodHooks
    }

    override fun flow(environment: SymbolProcessorEnvironment, extensions: Map<String, Any>) {
        val tempExtensions = extensions.toMutableMap()
        val basePackageName = extensions[BASE_PACKAGE_NAME] as String
        val serviceName = extensions[SERVICE_NAME] as String
        tempExtensions[SERVICE_NAME_PLURAL] = serviceName + "s"

        val components = Component.values()
        components.forEach { component ->
            val componentName = component.name.lowercase().toCamelCase()
            val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${componentName.capitalizeFirstLetter()}"
            val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
            val filePath = "${packageName.replace('.', '/')}/$className"

            tempExtensions[CLASS_NAME] = className
            tempExtensions[PACKAGE_NAME] = packageName
            tempExtensions[FILE_PATH] = filePath

            componentToTypeMapping[component] = initialize(component, tempExtensions)
        }

//        val methodRequests = arrayOf(MethodRequest.GET, MethodRequest.POST, MethodRequest.PUT, MethodRequest.DELETE, MethodRequest.GET_ALL)
        val methodRequests = arrayOf(MethodRequest.GET)
        methodRequests.forEach { methodRequest ->
            components.forEach { component ->
                val classSpec = componentToTypeMapping[component]
                if (classSpec != null) {
                    val componentName = component.name.lowercase().toCamelCase()
                    val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${componentName.capitalizeFirstLetter()}"
                    val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
                    val filePath = "${packageName.replace('.', '/')}/$className"

                    tempExtensions[CLASS_NAME] = className
                    tempExtensions[PACKAGE_NAME] = packageName
                    tempExtensions[FILE_PATH] = filePath

                    val function = flow(component, methodRequest, tempExtensions)
                    if (function != null) {
                        classSpec.addFunction(function.build())
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
            val className = "${serviceName.toCamelCase().capitalizeFirstLetter()}${componentName.capitalizeFirstLetter()}"
            val packageName = "$basePackageName.${serviceName.toCamelCase()}.${componentName}"
            val filePath = "${packageName.replace('.', '/')}/$className"

            val classSpec = componentToTypeMapping[component]
            if (classSpec == null)
                throw Exception("Class is missing for component: ${component}")

            val fileSpec = FileSpec
                .builder(packageName, className)
                .addType(classSpec.build())
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

    fun initialize(component: Component, extensions: Map<String, Any>): TypeSpec.Builder {
        val methodHook = findClassHook(component)
        return methodHook.hook(component, extensions)
    }

    fun flow(
        component: Component,
        methodRequest: MethodRequest,
        extensions: Map<String, Any>
    ): FunSpec.Builder? {
        val methodHook = findMethodHook(component, methodRequest)
        return methodHook?.hook(component, methodRequest, extensions)
    }

    fun findClassHook(component: Component): IClassHook {
        val hook = classHooks[component]
        if (hook == null)
            throw Exception("Class hook is missing for component: ${component}")

        return hook
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