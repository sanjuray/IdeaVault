package com.practice.ideavault

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.practice.ideavault.database.login_database_helper.LoginDatabaseHelper
import com.practice.ideavault.databinding.ActivityMainBinding
import com.practice.ideavault.fragment.notes_home.NotesHomeFragment
import com.practice.ideavault.fragment.sign_in.SigningInFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val sharedPrefName = "vault"
    private val keyEmail = "email"
    private val keyName = "name"

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var loginDatabaseHelper: LoginDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(sharedPrefName, MODE_PRIVATE)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@MainActivity, gso)

        val acct = GoogleSignIn.getLastSignedInAccount(this)

        loginDatabaseHelper = LoginDatabaseHelper(this@MainActivity)

        val email = sharedPreferences.getString(keyEmail,null)
        if(email == null && acct == null){
            signOut()
            loadFragment(SigningInFragment.newInstance())
        }else{
            makeToast(this,"Welcome back ${getLoggedInUserName()}!")
            loadFragment((NotesHomeFragment.newInstance()))
        }
    }

    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK)
        {
            val data = result.data ?: return@registerForActivityResult

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                logInUser(account.email.toString(),account.displayName.toString())
                // Handle successful sign-in with account
                navigateToNotesHome()
            } catch (e: ApiException) {
                Log.e("SGERROR", "Sign-in error: $e")
                Toast.makeText(applicationContext, "Unable to Login, Try Again!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getLoggedInUserEmail(): String{
        return sharedPreferences.getString(keyEmail,"").toString()
    }

    fun getLoggedInUserName(): String{
        return sharedPreferences.getString(keyName,"").toString()
    }

    private fun navigateToSignInPage(){
        loadFragment(SigningInFragment.newInstance())
    }

    private fun navigateToNotesHome(){
        loadFragment(NotesHomeFragment.newInstance())
    }

    fun loadFragment(
        fragment: Fragment?
    ) {
        try {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
//                fragmentTransaction.setCustomAnimations()
            if(fragment is SigningInFragment) {
                fragmentTransaction.add(
                    binding.containerForFragments.id,
                    fragment
                )
            }else{
                fragmentTransaction.replace(
                    binding.containerForFragments.id,
                    fragment!!
                )
                fragmentTransaction.addToBackStack(fragment.javaClass.canonicalName)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e("SGERROR","error while loading fragment: $e")
        }
    }

    fun signInUser(){
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun logInUser(email: String, displayName: String){
        val editor = sharedPreferences.edit()
        editor.putString(keyEmail,email)
        editor.putString(keyName,displayName)
        editor.apply()
        if(loginDatabaseHelper.isLogin(email)){
            makeToast(this, "Welcome back $displayName!")
        }else{
            makeToast(this,"Welcome $displayName, Thank you for Signing Up!")
            loginDatabaseHelper.insertUser(email,displayName)
        }
    }

    fun signOut(){
        googleSignInClient.signOut().addOnCompleteListener{
            logOutUser()
            makeToast(applicationContext,"Successfully signed out!")
            navigateToSignInPage()
        }
    }

    private fun logOutUser(){
        val editor = sharedPreferences.edit()
        editor.putString(keyEmail,null)
        editor.putString(keyName,null)
        editor.apply()
    }

    fun makeToast(context: Context, msg: String){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
    }

    fun getCurrentDate(): String{
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy")
        return current.format(formatter)
    }

    fun exitApplication(){
        finish()
    }
}