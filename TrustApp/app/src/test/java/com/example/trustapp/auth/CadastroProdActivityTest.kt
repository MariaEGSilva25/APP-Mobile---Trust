package com.example.trustapp.auth

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CadastroProdutorManagerTest {

 private lateinit var manager: CadastroProdutorManager

 @Before
 fun setUp() {
  manager = CadastroProdutorManager()
 }

 @Test
 fun `validaCampos retorna true quando todos os campos estão preenchidos`() {
  val resultado = manager.validarCampos(
   razao = "Razão Social",
   fantasia = "Nome Fantasia",
   cnpj = "12345678000100",
   email = "email@teste.com",
   senha = "senha123"
  )

  assertTrue(resultado)
 }

 @Test
 fun `validaCampos retorna false quando algum campo está vazio`() {
  val resultado = manager.validarCampos(
   razao = "Razão Social",
   fantasia = "",
   cnpj = "12345678000100",
   email = "email@teste.com",
   senha = "senha123"
  )

  assertFalse(resultado)
 }

 @Test
 fun `criarProdutor cria objeto Produtor corretamente`() {
  val produtor = manager.criarProdutor(
   razao = "Razão Social",
   fantasia = "Nome Fantasia",
   cnpj = "12345678000100",
   email = "email@teste.com"
  )

  assertEquals("Razão Social", produtor.razaoSocial)
  assertEquals("Nome Fantasia", produtor.nomeFantasia)
  assertEquals("12345678000100", produtor.CNPJ)
  assertEquals("email@teste.com", produtor.email)
 }
}
