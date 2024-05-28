package com.bruno.pontointeligente.controllers

import com.bruno.pontointeligente.documents.Funcionario
import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.dtos.LancamentoDto
import com.bruno.pontointeligente.enums.PerfilEnum
import com.bruno.pontointeligente.enums.TipoEnum
import com.bruno.pontointeligente.response.Response
import com.bruno.pontointeligente.services.FuncionarioService
import com.bruno.pontointeligente.services.LancamentoService
import com.bruno.pontointeligente.utils.LancamentoConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations.openMocks
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import java.text.SimpleDateFormat

@ExtendWith(SpringExtension::class)
@SpringBootTest
class LancamentoControllerTest {

    @MockBean
    private lateinit var lancamentoService: LancamentoService

    @MockBean
    private lateinit var funcionarioService: FuncionarioService

    private lateinit var lancamentoController: LancamentoController

    private lateinit var lancamento: Lancamento
    private lateinit var lancamentoDto: LancamentoDto
    private lateinit var funcionario: Funcionario
    private val lancamentoId = "665564eb3cf4db4f7ea30cy8"
    private val funcionarioId = "665564eb3cf4db4f7ea30c95"

    @Value("\${paginacao.qtd_por_pagina}")
    private val qtdPorPagina: Int = 15

    @BeforeEach
    fun setUp() {
        openMocks(this)
        lancamentoController = LancamentoController(lancamentoService, funcionarioService)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = dateFormat.parse("2022-05-28 08:00:00")
        lancamento = Lancamento(
            data,
            TipoEnum.INICIO_TRABALHO,
            funcionarioId,
            "descrição",
            "localização",
            lancamentoId
        )
        lancamentoDto = LancamentoDto(
            "2022-05-28 08:00:00",
            TipoEnum.INICIO_TRABALHO.toString(),
            "descrição",
            "localização",
            funcionarioId,
            lancamentoId
        )
        funcionario = Funcionario(
            nome = "Test Funcionario",
            email = "funcionario@test.com",
            senha = "123456",
            cpf = "12345678900",
            perfil = PerfilEnum.ROLE_USUARIO,
            empresaId = "empresaId",
            id = funcionarioId
        )
    }

    @Test
    fun `listarPorId deve retornar lancamento quando lancamento existe`() {
        `when`(lancamentoService.buscarPorId(lancamentoId)).thenReturn(lancamento)

        val response: ResponseEntity<Response<LancamentoDto>> = lancamentoController.listarPorId(lancamentoId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lancamentoId, response.body?.data?.id)
    }

    @Test
    fun `listarPorId deve retornar erro quando lancamento nao existe`() {
        `when`(lancamentoService.buscarPorId(lancamentoId)).thenReturn(null)

        val response: ResponseEntity<Response<LancamentoDto>> = lancamentoController.listarPorId(lancamentoId)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertTrue(response.body?.erros?.contains("Lançamento não encontrado para o id $lancamentoId") ?: false)
    }

    @Test
    fun `listarPorFuncionarioId deve retornar lancamentos para o funcionario`() {
        val lancamentosList = listOf(lancamento)
        val pageRequest = PageRequest.of(0, 15, Sort.Direction.DESC, "id")
        val pageLancamentos = PageImpl<Lancamento>(lancamentosList, pageRequest, lancamentosList.size.toLong())

        `when`(lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest)).thenReturn(pageLancamentos)

        val responseEntity: ResponseEntity<Response<Page<LancamentoDto>>> =
            lancamentoController.listarPorFuncionarioId(funcionarioId, 0, "id", "DESC")

        assertEquals(HttpStatus.OK, responseEntity.statusCode)

        val lancamentosDto = responseEntity.body?.data
        assertEquals(1, lancamentosDto?.content?.size)
        assertEquals(lancamento.id, lancamentosDto?.content?.get(0)?.id)
    }

    @Test
    fun `adicionar deve criar lancamento quando dados validos`() {
        val bindingResult: BindingResult = BeanPropertyBindingResult(lancamentoDto, "lancamentoDto")

        `when`(funcionarioService.buscarPorId(funcionarioId)).thenReturn(mock())
        `when`(lancamentoService.persistir(lancamento)).thenReturn(lancamento)

        val response: ResponseEntity<Response<LancamentoDto>> =
            lancamentoController.adicionar(lancamentoDto, bindingResult)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lancamento.id, response.body?.data?.id)
    }

    @Test
    fun `atualizar deve alterar lancamento quando dados validos`() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val bindingResult: BindingResult = BeanPropertyBindingResult(lancamentoDto, "lancamentoDto")

        `when`(funcionarioService.buscarPorId(funcionarioId)).thenReturn(funcionario)
        `when`(lancamentoService.buscarPorId(lancamentoId)).thenReturn(lancamento)
        `when`(lancamentoService.persistir(lancamento)).thenReturn(lancamento)


        `when`(
            LancamentoConverter.converterDtoParaLancamento(
                lancamentoDto,
                bindingResult,
                lancamentoService
            )
        ).thenReturn(lancamento)

        val response: ResponseEntity<Response<LancamentoDto>> =
            lancamentoController.atualizar(lancamentoId, lancamentoDto, bindingResult)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(lancamento.id, response.body?.data?.id)
        assertEquals(lancamento.data, dateFormat.parse(response.body?.data?.data))
        assertEquals(lancamento.funcionarioId, response.body?.data?.funcionarioId)
        assertEquals(lancamento.tipo.toString(), response.body?.data?.tipo)
        assertEquals(lancamento.localizacao, response.body?.data?.localizacao)
        assertEquals(lancamento.descricao, response.body?.data?.descricao)
    }

    @Test
    fun `remover deve excluir lancamento quando lancamento existe`() {
        `when`(lancamentoService.buscarPorId(lancamentoId)).thenReturn(lancamento)
        Mockito.doNothing().`when`(lancamentoService).remover(lancamentoId)

        val response: ResponseEntity<Response<String>> = lancamentoController.remover(lancamentoId)

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `remover deve retornar erro quando lancamento nao existe`() {
        `when`(lancamentoService.buscarPorId(lancamentoId)).thenReturn(null)

        val response: ResponseEntity<Response<String>> = lancamentoController.remover(lancamentoId)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertTrue(
            response.body?.erros?.contains("Erro ao remover lançamento. Registro não encontrado para o id $lancamentoId")
                ?: false
        )
    }
}