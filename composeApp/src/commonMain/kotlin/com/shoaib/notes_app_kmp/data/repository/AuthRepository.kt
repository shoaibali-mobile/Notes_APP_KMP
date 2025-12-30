package com.shoaib.notes_app_kmp.data.repository

import com.shoaib.notes_app_kmp.data.local.UserDao
import com.shoaib.notes_app_kmp.data.local.UserEntity
import java.security.MessageDigest


interface AuthRepository {
    suspend fun login(username: String, password: String): UserEntity?
    suspend fun signup(username: String, password: String): UserEntity
    fun hashPassword(password: String): String
}

class AuthRepositoryImpl(
    private val userDao: UserDao
): AuthRepository{
    override suspend fun login(
        username: String,
        password: String
    ): UserEntity? {
       val user = userDao.getUserByUsername(username)?: return null
      val passwordHash = hashPassword(password)
        return if (user.passwordHash == passwordHash) {
            user
        } else {
            null
        }
    }

    override suspend fun signup(username: String, password: String): UserEntity {
        // Check if user already exists
        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            throw IllegalArgumentException("Username already exists")
        }

        val passwordHash = hashPassword(password)
        val newUser = UserEntity(
            username = username,
            passwordHash = passwordHash
        )

        userDao.insertUser(newUser)
        return userDao.getUserByUsername(username)!!
    }


    override fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}