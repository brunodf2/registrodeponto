package com.bruno.pontointeligente.utils

import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.enums.TipoEnum
import com.bruno.pontointeligente.services.LancamentoService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*
import org.mockito.Mockito.*
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError


class LancamentoConverterTest {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Test
    fun `converterLancamentoDto should convert Lancamento to LancamentoDto`() {
        val lancamento = Lancamento(
            Date(), TipoEnum.INICIO_TRABALHO, "FuncionarioID", "Descricao", "Localizacao", "LancamentoID"
        )

        val lancamentoDto = LancamentoConverter.converterLancamentoDto(lancamento)

        assertEquals(dateFormat.format(lancamento.data), lancamentoDto.data)
        assertEquals(lancamento.tipo.toString(), lancamentoDto.tipo)
        assertEquals(lancamento.descricao, lancamentoDto.descricao)
        assertEquals(lancamento.localizacao, lancamentoDto.localizacao)
        assertEquals(lancamento.funcionarioId, lancamentoDto.funcionarioId)
        assertEquals(lancamento.id, lancamentoDto.id)
    }

    @Test
    fun `converterDtoParaLancamento should convert LancamentoDto to Lancamento`() {
        val lancamentoService = mock(LancamentoService::class.java)
        val bindingResult = mock(BindingResult::class.java)

        val lancamentoDto = LancamentoDto(
            dateFormat.format(Date()), TipoEnum.INICIO_TRABALHO.toString(),
            "Descricao", "Localizacao", "FuncionarioID", "LancamentoID"
        )

        `when`(lancamentoService.buscarPorId(lancamentoDto.id!!)).thenReturn(null)

        val lancamento = LancamentoConverter.converterDtoParaLancamento(lancamentoDto, bindingResult, lancamentoService)

        assertEquals(dateFormat.parse(lancamentoDto.data), lancamento.data)
        assertEquals(TipoEnum.valueOf(lancamentoDto.tipo!!), lancamento.tipo)
        assertEquals(lancamentoDto.descricao, lancamento.descricao)
        assertEquals(lancamentoDto.localizacao, lancamento.localizacao)
        assertEquals(lancamentoDto.funcionarioId, lancamento.funcionarioId)
        assertEquals(lancamentoDto.id, lancamento.id)
    }

    @Test
    fun `converterDtoParaLancamento should add error if lancamento not found`() {
        val lancamentoService = mock(LancamentoService::class.java)
        val bindingResult = mock(BindingResult::class.java)

        val lancamentoDto = LancamentoDto(
            dateFormat.format(Date()), TipoEnum.INICIO_TRABALHO.toString(),
            "Descricao", "Localizacao", "FuncionarioID", "NonExistentID"
        )

        `when`(lancamentoService.buscarPorId(lancamentoDto.id!!)).thenReturn(null)

        LancamentoConverter.converterDtoParaLancamento(lancamentoDto, bindingResult, lancamentoService)

        verify(bindingResult).addError(ObjectError("lancamento", "Lançamento não encontrado."))
    }
}