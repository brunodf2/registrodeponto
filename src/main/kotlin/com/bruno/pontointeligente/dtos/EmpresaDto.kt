package com.bruno.pontointeligente.dtos

import org.bson.types.ObjectId

data class EmpresaDto (
        val razaoSocial: String,
        val cnpj: String,
        var id: String? = ObjectId().toHexString()
)