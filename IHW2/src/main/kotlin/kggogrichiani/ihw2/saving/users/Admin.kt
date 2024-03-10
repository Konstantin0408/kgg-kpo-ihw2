package kggogrichiani.ihw2.saving.users

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class Admin(
    @Transient private val adminUsername: String = "#",
    @Transient private val adminPassword: String = "#"
) : User(adminUsername, adminPassword)