package com.bruno.pontointeligente.dtos
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.bson.types.ObjectId
import org.hibernate.validator.constraints.Length

data class FuncionarioDto (
        @get:NotEmpty(message = "Nome não pode ser vazio.")
        @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
        val nome: String = "",

        @get:NotEmpty(message = "Email não pode ser vazio.")
        @get:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
        @get:Email(message="Email inválido.")
        val email: String = "",

        val senha: String? = null,
        val valorHora: String? = null,
        val qtdHorasTrabalhoDia: String? = null,
        val qtdHorasAlmoco: String? = null,
        var id: String? = ObjectId().toHexString()
)