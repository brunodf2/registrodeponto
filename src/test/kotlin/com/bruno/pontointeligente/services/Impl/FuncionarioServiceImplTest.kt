package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Funcionario
import com.bruno.pontointeligente.enums.PerfilEnum
import com.bruno.pontointeligente.repositories.FuncionarioRepository
import com.bruno.pontointeligente.utils.SenhaUtils
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class FuncionarioServiceImplTest {

    @Mock
    private val funcionarioRepository: FuncionarioRepository? = null

    @InjectMocks
    private val funcionarioService: FuncionarioServiceImpl? = null

    lateinit var funcionario: Funcionario

    @BeforeEach
    fun setUp() {
        funcionario = Funcionario(
                id = ObjectId.get().toHexString(),
                nome = "João da Silva",
                cpf = "12345678900",
                email = "joao.silva@example.com",
                empresaId = "teste",
                senha = SenhaUtils().gerarBcrypt("12345"),
                perfil = PerfilEnum.ROLE_USUARIO
        )
    }

    @Test
    fun `deve persistir funcionario`() {
        `when`(funcionarioRepository?.save(funcionario)).thenReturn(funcionario)

        val resultado = funcionarioService?.persistir(funcionario)

        assertEquals(funcionario, resultado)
    }

    @Test
    fun `deve buscar funcionario pelo cpf`() {
        `when`(funcionarioRepository?.findByCpf(funcionario.cpf)).thenReturn(funcionario)

        val resultado = funcionarioService?.buscarPorCpf(funcionario.cpf)

        assertEquals(funcionario, resultado)
    }

    @Test
    fun `deve buscar funcionario pelo email`() {
        `when`(funcionarioRepository?.findByEmail(funcionario.email)).thenReturn(funcionario)

        val resultado = funcionarioService?.buscarPorEmail(funcionario.email)

        assertEquals(funcionario, resultado)
    }

    @Test
    fun `deve buscar funcionario pelo ID`() {
        `when`(funcionarioRepository?.findById(funcionario.id!!)).thenReturn(Optional.of(funcionario))

        val resultado = funcionarioService?.buscarPorId(funcionario.id.toString())

        assertEquals(funcionario, resultado)
    }
}