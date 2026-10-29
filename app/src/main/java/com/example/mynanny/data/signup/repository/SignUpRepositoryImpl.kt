package com.example.mynanny.data.signup.repository

import com.example.mynanny.data.common.UserMetadata
import com.example.mynanny.data.common.mapAuthErrors
import com.example.mynanny.data.signup.remote.SignUpRemoteDataSource
import com.example.mynanny.domain.signup.SignUpRequest
import com.example.mynanny.domain.signup.SignUpResult
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val remoteDataSource: SignUpRemoteDataSource,
) : SignUpRepository {

    override suspend fun signUp(request: SignUpRequest): SignUpResult = mapAuthErrors {
        val metadata = buildJsonObject {
            put(UserMetadata.FIRST_NAME, request.firstName)
            put(UserMetadata.LAST_NAME, request.lastName)
            put(UserMetadata.PHONE, request.phone)
        }
        val signedIn = remoteDataSource.signUp(request.email, request.password, metadata)
        if (signedIn) SignUpResult.SIGNED_IN else SignUpResult.EMAIL_CONFIRMATION_REQUIRED
    }
}
