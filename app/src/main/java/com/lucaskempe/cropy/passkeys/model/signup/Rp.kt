package com.lucaskempe.cropy.passkeys.model.signup

import com.google.gson.annotations.SerializedName


data class Rp (

  @SerializedName("name" ) var name : String? = null,
  @SerializedName("id"   ) var id   : String? = null

)