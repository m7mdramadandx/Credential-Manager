package com.example.credential_manager

import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.credentials.*
import androidx.credentials.exceptions.*
import androidx.credentials.exceptions.publickeycredential.CreatePublicKeyCredentialDomException
import androidx.lifecycle.lifecycleScope
import com.example.credential_manager.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    val TAG = "CredentialTest"
    var responseJson = ""
    private lateinit var binding: ActivityMainBinding


    private val credentialManager by lazy {
        CredentialManager.create(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.btnSignin.setOnClickListener {
            lifecycleScope.launch {
                getCredential()
            }
        }

        binding.btnSignup.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            lifecycleScope.launch {
                if (username.isNotBlank() && password.isNotBlank()) {
                    saveCredential(username, password)
                }
            }
        }

        binding.btnClear.setOnClickListener {
            clearCredential()
        }
    }

    private suspend fun getCredential() {
        try {
            //Tell the credential library that we're only interested in password credentials
            val getCredRequest = GetCredentialRequest(listOf(GetPasswordOption()))

            //Show the user a dialog allowing them to pick a saved credential
            val credentialResponse = credentialManager.getCredential(
                request = getCredRequest,
                activity = this,
            )
            handleSignIn(credentialResponse)

        } catch (e: GetCredentialCancellationException) {
            //User cancelled the request. Return nothing
            Log.e("CredentialTest", "Error getting credential", e)
        } catch (e: NoCredentialException) {
            //We don't have a matching credential
            Log.e("CredentialTest", "Error getting credential", e)
        } catch (e: GetCredentialException) {
            Log.e("CredentialTest", "Error getting credential", e)
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            is PublicKeyCredential -> {
                responseJson = credential.authenticationResponseJson
                fidoAuthenticateWithServer(responseJson)
            }
            is PasswordCredential -> {
                val username = credential.id
                val password = credential.password
                passwordAuthenticateWithServer(username, password)
            }
            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun passwordAuthenticateWithServer(username: String, password: String) {
        Snackbar.make(
            binding.root,
            "username: $username ----- password: $password",
            Snackbar.LENGTH_LONG
        ).show()

        Log.i(TAG, "username/password: ${username.plus("// $password")}")

    }

    private fun fidoAuthenticateWithServer(responseJson: Any) {
        Log.i(TAG, "responseJson: $responseJson")
    }

    private suspend fun saveCredential(username: String, password: String) {
        try {
            //Ask the user for permission to add the credentials to their store
            credentialManager.createCredential(
                request = CreatePasswordRequest(username, password),
                activity = this,
            )
            Snackbar.make(
                binding.root, "Credentials successfully added",
                Snackbar.LENGTH_LONG
            ).show()
        } catch (e: CreateCredentialCancellationException) {
            Log.e("CredentialTest", "User cancelled the save")
        } catch (e: CreateCredentialException) {
            handleFailure(e)
        }
    }

    private fun handleFailure(e: CreateCredentialException) {
        when (e) {
            is CreatePublicKeyCredentialDomException -> {
                Log.w(TAG, "error0 ${e.errorMessage}")
            }
            is CreateCredentialCancellationException -> {
                Log.w(TAG, "error1 ${e.errorMessage}")
            }
            is CreateCredentialInterruptedException -> {
                Log.w(TAG, "error2 ${e.errorMessage}")
            }
            is CreateCredentialProviderConfigurationException -> {
                Log.w(TAG, "error3 ${e.errorMessage}")
            }
            is CreateCredentialUnknownException -> {
                Log.w(TAG, "error4 ${e.errorMessage}")
            }
            is CreateCustomCredentialException -> {
                Log.w(TAG, "error5 ${e.errorMessage}")
            }
            else -> Log.w(TAG, "Unexpected exception type ${e::class.java.name}")
        }
    }

    private fun clearCredential() {
        credentialManager.clearCredentialStateAsync(
            request = ClearCredentialStateRequest(),
            cancellationSignal = CancellationSignal(),
            executor = {},
            callback = object : CredentialManagerCallback<Void?, ClearCredentialException> {
                override fun onError(e: ClearCredentialException) {
                    Snackbar.make(binding.root, e.errorMessage.toString(), Snackbar.LENGTH_LONG)
                        .show()

                }

                override fun onResult(result: Void?) {
                    Snackbar.make(
                        binding.root, "clear credentials successfully",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        )

    }

}