package io.github.akiomik.seiun.viewmodel

import io.github.akiomik.seiun.SeiunApplication
import io.github.akiomik.seiun.model.ISession

class LoginViewModel : ApplicationViewModel() {
    private val userRepository = SeiunApplication.instance!!.userRepository

    fun login(
        handle: String,
        password: String,
        onSuccess: (ISession) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        wrapError(run = {
            userRepository.login(handle, password)
        }, onSuccess = onSuccess, onError = onError)
    }

    fun getLoginParam(): Pair<String, String> {
        return userRepository.getLoginParam()
    }
}
