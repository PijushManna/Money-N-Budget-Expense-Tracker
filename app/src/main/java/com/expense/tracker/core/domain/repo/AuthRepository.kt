package com.expense.tracker.core.domain.repo

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun signIn(): Result<FirebaseUser>
    suspend fun signOut()
}