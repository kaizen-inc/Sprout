package inc.kaizen.service.sprout.hooks

enum class Component(val packageName: String) {
    CONTROLLER("controller"),
    SERVICE("service"),
    REPOSITORY("repository"),
    CONVERTER("converter"),
    ENTITY("model.entity")
}
