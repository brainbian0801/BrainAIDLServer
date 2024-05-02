package com.example.brainaidlserver.activity.data

import com.example.brainaidlserver.activity.data.model.LoggedInUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication
            val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    fun taskA(): String {
        return runBlocking {
            delay(2000)
            "Brain"
        }
    }

    fun taskB(): Int {
        return runBlocking {
            delay(4000)
            10
        }
    }

    fun taskC(): LoggedInUser {
        return runBlocking {
            delay(6000)
            LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
        }
    }
}