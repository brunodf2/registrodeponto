package com.bruno.pontointeligente.utils

import com.bruno.pontointeligente.documents.Funcionario
import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.services.FuncionarioService
import com.bruno.pontointeligente.utils.Validador.validarFuncionario
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

class ValidadorTest {
    @Test
    fun `validarFuncionario deve adicionar erro se funcionarioId for nulo`() {
        val funcionarioService = mock(FuncionarioService::class.java)
        val bindingResult = mock(BindingResult::class.java)

        val lancamentoDto = LancamentoDto(
            "2024-05-28 12:00:00", "TIPO", "Descrição", "Localização", null, "ID"
        )

        validarFuncionario(lancamentoDto, bindingResult, funcionarioService)

        verify(bindingResult).addError(ObjectError("funcionario", "Funcionário não informado."))
    }

    @Test
    fun `validarFuncionario deve adicionar erro se funcionario não for encontrado`() {
        val funcionarioService = mock(FuncionarioService::class.java)
        val bindingResult = mock(BindingResult::class.java)

        val lancamentoDto = LancamentoDto(
            "2024-05-28 12:00:00", "TIPO", "Descrição", "Localização", "ID", "ID"
        )

        `when`(funcionarioService.buscarPorId(lancamentoDto.funcionarioId!!)).thenReturn(null)

        validarFuncionario(lancamentoDto, bindingResult, funcionarioService)

        verify(bindingResult).addError(ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."))
    }

    @Test
    fun `validarFuncionario não deve adicionar erro se funcionario for encontrado`() {
        val funcionarioService = mock(FuncionarioService::class.java)
        val bindingResult = mock(BindingResult::class.java)

        val lancamentoDto = LancamentoDto(
            "2024-05-28 12:00:00", "TIPO", "Descrição", "Localização", "ID", "ID"
        )
        val funcionario = Funcionario(
            id = "ID",
            nome = "Nome",
            email = "email@example.com",
            senha = "senha",
            cpf = "12345678901",
            perfil = com.bruno.pontointeligente.enums.PerfilEnum.ROLE_USUARIO,
            empresaId = "EmpresaID",
            valorHora = null,
            qtdHorasTrabalhoDia = null,
            qtdHorasAlmoco = null
        )

        `when`(funcionarioService.buscarPorId(lancamentoDto.funcionarioId!!)).thenReturn(funcionario)

        validarFuncionario(lancamentoDto, bindingResult, funcionarioService)

        verify(bindingResult, never()).addError(any(ObjectError::class.java))
    }
}