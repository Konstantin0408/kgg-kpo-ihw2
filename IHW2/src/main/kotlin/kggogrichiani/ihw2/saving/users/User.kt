package kggogrichiani.ihw2.saving.users

import kotlinx.serialization.Serializable

@Serializable
sealed class User(var username: String, var password: String)
