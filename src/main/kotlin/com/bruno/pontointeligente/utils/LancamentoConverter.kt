package com.bruno.pontointeligente.utils

import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.enums.TipoEnum
import com.bruno.pontointeligente.services.LancamentoService
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import java.text.SimpleDateFormat

object LancamentoConverter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    fun converterLancamentoDto(lancamento: Lancamento): LancamentoDto {
        return LancamentoDto(
            dateFormat.format(lancamento.data), lancamento.tipo.toString(),
            lancamento.descricao, lancamento.localizacao,
            lancamento.funcionarioId, lancamento.id
        )
    }

    fun converterDtoParaLancamento(
        lancamentoDto: LancamentoDto,
        result: BindingResult,
        lancamentoService: LancamentoService
    ): Lancamento {
        if (lancamentoDto.id != null) {
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDto.id!!)
            if (lanc == null) result.addError(ObjectError("lancamento", "Lançamento não encontrado."))
        }

        return Lancamento(
            dateFormat.parse(lancamentoDto.data), TipoEnum.valueOf(lancamentoDto.tipo!!),
            lancamentoDto.funcionarioId!!, lancamentoDto.descricao,
            lancamentoDto.localizacao, lancamentoDto.id
        )
    }
}