package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Funcionario
import com.bruno.pontointeligente.repositories.FuncionarioRepository
import com.bruno.pontointeligente.services.FuncionarioService
import org.springframework.stereotype.Service

@Service
class FuncionarioServiceImpl(
        val funcionarioRepository: FuncionarioRepository
) : FuncionarioService {
    override fun persistir(funcionario: Funcionario) = funcionarioRepository.save(funcionario)

    override fun buscarPorCpf(cpf: String) = funcionarioRepository.findByCpf(cpf)

    override fun buscarPorEmail(email: String) = funcionarioRepository.findByEmail(email)

    override fun buscarPorId(id: String) = funcionarioRepository.findById(id).orElse(null)
}