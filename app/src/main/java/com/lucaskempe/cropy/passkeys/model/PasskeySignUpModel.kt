package com.lucaskempe.cropy.passkeys.model

import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.passkeys.model.signup.AuthenticatorSelection
import com.lucaskempe.cropy.passkeys.model.signup.ExcludeCredentials
import com.lucaskempe.cropy.passkeys.model.signup.PubKeyCredParams
import com.lucaskempe.cropy.passkeys.model.signup.Rp
import com.lucaskempe.cropy.passkeys.model.signup.User


data class PasskeySignUpModel (

  @SerializedName("challenge"              ) var challenge              : String?                       = null,
  @SerializedName("rp"                     ) var rp                     : Rp?                           = Rp(),
  @SerializedName("user"                   ) var user                   : User?                         = User(),
  @SerializedName("pubKeyCredParams"       ) var pubKeyCredParams       : ArrayList<PubKeyCredParams>   = arrayListOf(),
  @SerializedName("timeout"                ) var timeout                : Int?                          = null,
  @SerializedName("attestation"            ) var attestation            : String?                       = null,
  @SerializedName("excludeCredentials"     ) var excludeCredentials     : ArrayList<ExcludeCredentials> = arrayListOf(),
  @SerializedName("authenticatorSelection" ) var authenticatorSelection : AuthenticatorSelection?       = AuthenticatorSelection()

)