package com.example.trustapp.auth

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CadastroUserManagerTest {

 private lateinit var manager: CadastroUserManager

 @Before
 fun setup() {
  manager = CadastroUserManager()
 }

 @Test
 fun `validaCampos retorna true quando todos os campos estao preenchidos`() {
  val resultado = manager.validarCampos("Nome", "email@teste.com", "01/01/2000", "Endereco", "senha123")
  assertTrue(resultado)
 }

 @Test
 fun `validaCampos retorna false quando algum campo esta vazio`() {
  val resultado = manager.validarCampos("", "email@teste.com", "01/01/2000", "Endereco", "senha123")
  assertFalse(resultado)
 }

 @Test
 fun `criarUsuario cria objeto User corretamente`() {
  val user = manager.criarUsuario("Nome", "email@teste.com", "01/01/2000", "Endereco")
  assertEquals("Nome", user.nomeCompleto)
  assertEquals("email@teste.com", user.email)
  assertEquals("01/01/2000", user.dataDeNascimento)
  assertEquals("Endereco", user.endereco)
 }
}
