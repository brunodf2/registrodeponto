package com.bruno.pontointeligente.services.Impl

import com.bruno.pontointeligente.documents.Lancamento
import com.bruno.pontointeligente.repositories.LancamentoRepository
import com.bruno.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LancamentoServiceImpl(
        val lancamentoRepository: LancamentoRepository
) : LancamentoService {
    override fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest) =
            lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest)

    override fun buscarPorId(id: String) = lancamentoRepository.findById(id).orElse(null)

    override fun persistir(lancamento: Lancamento) = lancamentoRepository.save(lancamento)

    override fun remover(id: String) = lancamentoRepository.deleteById(id)
}