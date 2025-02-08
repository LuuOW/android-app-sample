package com.lucaskempe.cropy.passkeys.model.signup

import com.google.gson.annotations.SerializedName


data class ExcludeCredentials (

  @SerializedName("id"   ) var id   : String? = null,
  @SerializedName("type" ) var type : String? = null

)