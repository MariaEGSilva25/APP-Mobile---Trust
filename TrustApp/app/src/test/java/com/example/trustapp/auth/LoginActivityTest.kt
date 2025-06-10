package com.example.trustapp.auth

import com.google.firebase.auth.FirebaseAuth
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito.*

class LoginActivityTest {

  private lateinit var auth: FirebaseAuth
  private lateinit var loginManager: LoginManager

  @Before
  fun setup() {
   auth = mock(FirebaseAuth::class.java)
   loginManager = LoginManager(auth)
  }

  @Test
  fun `validarCampos retorna true quando email e senha preenchidos`() {
   val resultado = loginManager.validarCampos("user@email.com", "senha123")
   assertTrue(resultado)
  }

  @Test
  fun `validarCampos retorna false quando email vazio`() {
   val resultado = loginManager.validarCampos("", "senha123")
   assertFalse(resultado)
  }

  @Test
  fun `validarCampos retorna false quando senha vazia`() {
   val resultado = loginManager.validarCampos("user@email.com", "")
   assertFalse(resultado)
  }
}