package com.bruno.pontointeligente.controllers

import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.response.Response
import com.bruno.pontointeligente.services.FuncionarioService
import com.bruno.pontointeligente.services.LancamentoService
import com.bruno.pontointeligente.utils.LancamentoConverter.converterDtoParaLancamento
import com.bruno.pontointeligente.utils.LancamentoConverter.converterLancamentoDto
import com.bruno.pontointeligente.utils.Validador.validarFuncionario
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(
    val lancamentoService: LancamentoService,
    val funcionarioService: FuncionarioService
) {

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPagina: Int = 15

    @GetMapping("/{id}")
    fun listarPorId(@PathVariable("id") id: String): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Lançamento não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/funcionario/{funcionarioId}")
    fun listarPorFuncionarioId(
        @PathVariable("funcionarioId") funcionarioId: String,
        @RequestParam(value = "pag", defaultValue = "0") pag: Int,
        @RequestParam(value = "ord", defaultValue = "id") ord: String,
        @RequestParam(value = "dir", defaultValue = "DESC") dir: String,
    ): ResponseEntity<Response<Page<LancamentoDto>>> {
        val response: Response<Page<LancamentoDto>> = Response<Page<LancamentoDto>>()

        val pageRequest: PageRequest = PageRequest.of(pag, qtdPorPagina, Sort.Direction.valueOf(dir), ord)
        val lancamentos: Page<Lancamento> =
            lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)

        val lancamentoDto: Page<LancamentoDto>? =
            lancamentos.map { lancamento -> converterLancamentoDto(lancamento) }

        response.data = lancamentoDto
        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun adicionar(
        @Valid @RequestBody lancamentoDto: LancamentoDto,
        result: BindingResult
    ): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        validarFuncionario(lancamentoDto, result, funcionarioService)

        if (result.hasErrors()) {
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDto, result, lancamentoService)
        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable("id") id: String,
        @Valid
        @RequestBody lancamentoDto: LancamentoDto,
        result: BindingResult
    ): ResponseEntity<Response<LancamentoDto>> {
        val response: Response<LancamentoDto> = Response()
        validarFuncionario(lancamentoDto, result, funcionarioService)
        lancamentoDto.id = id
        val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDto, result, lancamentoService)


        if (result.hasErrors()) {
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }


        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDto(lancamento)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping(value = ["/{id}"])
    fun remover(@PathVariable("id") id: String): ResponseEntity<Response<String>> {
        val response: Response<String> = Response<String>()
        val lancamento: Lancamento? = lancamentoService.buscarPorId(id)

        if (lancamento == null) {
            response.erros.add("Erro ao remover lançamento. Registro não encontrado para o id $id")
            return ResponseEntity.badRequest().body(response)
        }

        lancamentoService.remover(id)
        return ResponseEntity.ok(Response<String>())
    }
}