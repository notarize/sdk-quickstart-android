package com.notarize.androidquickstart.networking

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignerRequest(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: PhoneNumberInfo
)

@JsonClass(generateAdapter = true)
data class SignerResponse(
    val signer_info: SignerInfo
)

@JsonClass(generateAdapter = true)
data class SignerInfo(
    val sdk_token: String
)

@JsonClass(generateAdapter = true)
data class PhoneNumberInfo(
    val country_code: String,
    val number: String
)

@JsonClass(generateAdapter = true)
data class DocumentResource(
    val resource: String,
    val customer_can_annotate: Boolean = true
)

@JsonClass(generateAdapter = true)
data class TransactionRequest(
    val documents: List<DocumentResource>,
    val signers: List<SignerRequest>,
    val require_secondary_photo_id: Boolean,
    val require_new_signer_verification: Boolean = false
)