package com.bruno.pontointeligente.dtos

import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId

data class LancamentoDto (
        @get:NotEmpty(message = "Data não pode ser vazia.")
        val data: String? = null,

        @get:NotEmpty(message = "Tipo não pode ser vazio.")
        val tipo: String? = null,

        val descricao: String? = null,
        val localizacao: String? = null,
        val funcionarioId: String? = null,
        var id: String? = ObjectId().toHexString()
)