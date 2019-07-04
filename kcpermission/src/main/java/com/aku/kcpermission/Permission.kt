package com.aku.kcpermission

class Permission constructor(
    val name: String,
    val granted: Boolean,
    val shouldShowRequest: Boolean = false
) {

    constructor(permissions: List<Permission>) : this(
        combineName(permissions),
        combineGranted(permissions),
        combineShouldShowRequestPermissionRationale(permissions)!!
    )

    override fun equals(other: Any?): Boolean {
        val that = other as? Permission ?: return false
        return granted == that.granted
                && shouldShowRequest == that.shouldShowRequest
                && name == that.name
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + if (granted) 1 else 0
        result = 31 * result + if (shouldShowRequest) 1 else 0
        return result
    }

    override fun toString(): String {
        return """Permission{name='$name', granted='$granted', shouldShowRequest='$shouldShowRequest'}"""

    }

    companion object {
        private fun combineName(permissions: List<Permission>): String {
            return permissions.joinToString(",") { it.name }
        }

        private fun combineGranted(permissions: List<Permission>): Boolean {
            return permissions.all { it.granted }
        }

        private fun combineShouldShowRequestPermissionRationale(permissions: List<Permission>): Boolean? {
            return permissions.any { it.shouldShowRequest }
        }
    }

}
