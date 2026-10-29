package com.example.mynanny.data.common

/**
 * Keys of the profile stored in the Supabase user metadata (`raw_user_meta_data`).
 * Written at sign-up, read by the session; also used by the `profiles` SQL trigger.
 */
object UserMetadata {
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val PHONE = "phone"
}
