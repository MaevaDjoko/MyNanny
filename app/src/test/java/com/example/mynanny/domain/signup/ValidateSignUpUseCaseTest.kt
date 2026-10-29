package com.example.mynanny.domain.signup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateSignUpUseCaseTest {

    private val validate = ValidateSignUpUseCase()
    private val valid = SignUpRequest("Marie", "Dupont", "marie@mail.fr", "", "motdepasse")

    @Test
    fun `a complete form is valid`() {
        assertTrue(validate(valid).isEmpty())
    }

    @Test
    fun `every invalid field is reported at once`() {
        val errors = validate(SignUpRequest(" ", "", "marie@", "123", "court"))
        assertEquals(
            setOf(
                SignUpFieldError.FIRST_NAME_REQUIRED,
                SignUpFieldError.LAST_NAME_REQUIRED,
                SignUpFieldError.EMAIL_INVALID,
                SignUpFieldError.PHONE_INVALID,
                SignUpFieldError.PASSWORD_TOO_SHORT,
            ),
            errors,
        )
    }

    @Test
    fun `phone is optional but checked when provided`() {
        assertTrue(validate(valid.copy(phone = "06 12 34 56 78")).isEmpty())
        assertTrue(validate(valid.copy(phone = "+33612345678")).isEmpty())
        assertEquals(setOf(SignUpFieldError.PHONE_INVALID), validate(valid.copy(phone = "06 12")))
    }

    @Test
    fun `password needs at least 8 characters`() {
        assertEquals(setOf(SignUpFieldError.PASSWORD_TOO_SHORT), validate(valid.copy(password = "1234567")))
        assertTrue(validate(valid.copy(password = "12345678")).isEmpty())
    }
}
