package com.bruno.pontointeligente.documents

import com.bruno.pontointeligente.enums.TipoEnum
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Lancamento (
        val data: Date,
        val tipo: TipoEnum,
        val funcionarioId:  String,
        val descricao: String? = "",
        val localizacao: String? = "",
        @Id val id: String? = ObjectId().toHexString()
)