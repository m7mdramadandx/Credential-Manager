package com.example.credential_manager.core

import javax.inject.Inject

open class BaseRepo {

    @Inject
    lateinit var networkManager: NetworkManager
}