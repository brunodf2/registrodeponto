package com.bruno.pontointeligente.documents

import com.bruno.pontointeligente.enums.PerfilEnum
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Funcionario (
        val nome: String,
        val email: String,
        val senha: String,
        val cpf: String,
        val perfil: PerfilEnum,
        val empresaId: String,
        val valorHora: Double? = 0.0,
        val qtdHorasTrabalhoDia: Float? = 0.0f,
        val qtdHorasAlmoco: Float? = 0.0f,
        @Id val id: String? = ObjectId().toHexString()
)