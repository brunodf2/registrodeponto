package com.bruno.pontointeligente.services

import com.bruno.pontointeligente.documents.Empresa

interface EmpresaService {

    fun buscarPorCnpj(cnpj: String): Empresa?

    fun persistir(empresa: Empresa): Empresa
}