package com.example.mynanny.domain.signup

import com.example.mynanny.data.signup.repository.SignUpRepository
import com.example.mynanny.domain.common.AuthException
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val validateSignUp: ValidateSignUpUseCase,
    private val signUpRepository: SignUpRepository,
) {
    /**
     * Cleans and validates the form, then creates the account.
     * @throws AuthException.InvalidForm when some fields are invalid (nothing is sent to the server)
     * @throws AuthException for server or network failures
     */
    suspend operator fun invoke(request: SignUpRequest): SignUpResult {
        val cleaned = request.copy(
            firstName = request.firstName.trim(),
            lastName = request.lastName.trim(),
            email = request.email.trim().lowercase(),
            phone = request.phone.filterNot(Char::isWhitespace),
        )
        val errors = validateSignUp(cleaned)
        if (errors.isNotEmpty()) throw AuthException.InvalidForm(errors)
        return signUpRepository.signUp(cleaned)
    }
}
