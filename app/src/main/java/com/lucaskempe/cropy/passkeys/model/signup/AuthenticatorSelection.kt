package com.lucaskempe.cropy.passkeys.model.signup

import com.google.gson.annotations.SerializedName


data class AuthenticatorSelection (

  @SerializedName("authenticatorAttachment" ) var authenticatorAttachment : String?  = null,
  @SerializedName("requireResidentKey"      ) var requireResidentKey      : Boolean? = null,
  @SerializedName("residentKey"             ) var residentKey             : String?  = null,
  @SerializedName("userVerification"        ) var userVerification        : String?  = null

)