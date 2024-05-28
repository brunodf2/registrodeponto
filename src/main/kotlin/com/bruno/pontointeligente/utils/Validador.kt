package com.bruno.pontointeligente.utils

import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.services.FuncionarioService
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

object Validador {
    fun validarFuncionario(
        lancamentoDto: LancamentoDto,
        result: BindingResult,
        funcionarioService: FuncionarioService
    ) {
        if (lancamentoDto.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionário não informado."))
            return
        }

        val funcionario = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if (funcionario == null) {
            result.addError(ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."))
        }
    }
}