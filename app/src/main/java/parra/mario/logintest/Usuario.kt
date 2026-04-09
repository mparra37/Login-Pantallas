package parra.mario.logintest

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Usuario(val username: String? = null, val email: String? = null, var edad: Int = 0) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}