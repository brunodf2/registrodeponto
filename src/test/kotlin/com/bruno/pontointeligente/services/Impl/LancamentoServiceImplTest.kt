package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.enums.TipoEnum
import com.bruno.pontointeligente.repositories.LancamentoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class LancamentoServiceImplTest {

    @Mock
    private val lancamentoRepository: LancamentoRepository? = null

    @InjectMocks
    private val lancamentoService: LancamentoServiceImpl? = null

    private lateinit var lancamento: Lancamento
    private lateinit var lancamentoPage: Page<Lancamento>

    @BeforeEach
    fun setUp() {
        lancamento = Lancamento(
                id = "1",
                data = Date(),
                tipo = TipoEnum.INICIO_TRABALHO,
                descricao = "Teste de lançamento",
                funcionarioId = "funcionario_1",
        )
        lancamentoPage = PageImpl(listOf(lancamento))
    }

    @Test
    fun `deve buscar lancamentos por funcionario ID`() {
        val pageRequest = PageRequest.of(0, 10)
        `when`(lancamentoRepository?.findByFuncionarioId("funcionario_1", pageRequest)).thenReturn(lancamentoPage)

        val resultado = lancamentoService?.buscarPorFuncionarioId("funcionario_1", pageRequest)

        assertEquals(lancamentoPage, resultado)
    }

    @Test
    fun `deve buscar lancamento por ID`() {
        `when`(lancamentoRepository?.findById("1")).thenReturn(Optional.of(lancamento))

        val resultado = lancamentoService?.buscarPorId("1")

        assertEquals(lancamento, resultado)
    }

    @Test
    fun `deve retornar null quando nao encontrar lancamento por ID`() {
        `when`(lancamentoRepository?.findById("999")).thenReturn(Optional.empty())

        val resultado = lancamentoService?.buscarPorId("999")

        assertNull(resultado)
    }

    @Test
    fun `deve persistir lancamento`() {
        `when`(lancamentoRepository?.save(lancamento)).thenReturn(lancamento)

        val resultado = lancamentoService?.persistir(lancamento)

        assertEquals(lancamento, resultado)
    }

    @Test
    fun `deve remover lancamento por ID`() {
        lancamentoService?.remover("1")

        verify(lancamentoRepository, Mockito.times(1))?.deleteById("1")
    }

}