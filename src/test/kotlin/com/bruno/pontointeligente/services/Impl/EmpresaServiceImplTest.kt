package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Empresa
import com.bruno.pontointeligente.repositories.EmpresaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class EmpresaServiceImplTest {

    @MockBean
    private lateinit var empresaRepository: EmpresaRepository

    @Autowired
    private lateinit var empresaService: EmpresaServiceImpl


    @Test
    fun `deve retornar empresa ao buscar por cnpj`() {
        val cnpj = "12345678901234"
        val empresa = Empresa(cnpj = cnpj, razaoSocial = "Empresa Teste")

        `when`(empresaRepository.findByCnpj(cnpj)).thenReturn(empresa)

        val result = empresaService.buscarPorCnpj(cnpj)

        assertNotNull(result)
        assertEquals(empresa, result)
    }

    @Test
    fun `deve retornar null ao buscar por cnpj que nao existe`() {
        val cnpj = "12345678901234"

        `when`(empresaRepository.findByCnpj(cnpj)).thenReturn(null)

        val result = empresaService.buscarPorCnpj(cnpj)

        assertNull(result)
    }

    @Test
    fun `deve salvar empresa com sucesso`() {
        val empresa = Empresa(cnpj = "12345678901234", razaoSocial = "Empresa Teste")

        `when`(empresaRepository.save(empresa)).thenReturn(empresa)

        val result = empresaService.persistir(empresa)

        assertNotNull(result)
        assertEquals(empresa, result)
    }
}