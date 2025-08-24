package com.loopr.data.model

data class UserProfile(
    val emailId: String = "",
    val name: String = "",
    val profileImageUrl: String = "",
    val web3AuthId: String = "" // Store Web3Auth user ID instead of private key
)
