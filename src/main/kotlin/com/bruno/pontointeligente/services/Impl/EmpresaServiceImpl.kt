package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Empresa
import com.bruno.pontointeligente.repositories.EmpresaRepository
import com.bruno.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(
        val empresaRepository: EmpresaRepository
) : EmpresaService {

    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)

    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}