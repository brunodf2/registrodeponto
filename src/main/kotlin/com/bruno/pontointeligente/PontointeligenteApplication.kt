package com.bruno.pontointeligente

import com.bruno.pontointeligente.documents.Empresa
import com.bruno.pontointeligente.documents.Funcionario
import com.bruno.pontointeligente.enums.PerfilEnum
import com.bruno.pontointeligente.repositories.EmpresaRepository
import com.bruno.pontointeligente.repositories.FuncionarioRepository
import com.bruno.pontointeligente.repositories.LancamentoRepository
import com.bruno.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = arrayOf(SecurityAutoConfiguration::class))
class PontoInteligenteApplication(
        val empresaRepository: EmpresaRepository,
        val funcionarioRepository: FuncionarioRepository,
        val lancamentoRepository: LancamentoRepository
) : CommandLineRunner {
    override fun run(vararg args: String?) {

        empresaRepository.deleteAll()
        funcionarioRepository.deleteAll()
        lancamentoRepository.deleteAll()

        var empresa: Empresa = Empresa(
                "Empresa",
                "1000234567890")
        empresaRepository.save(empresa)

        val admin: Funcionario = Funcionario(
                "Admin",
                "admin@empresa.com",
                SenhaUtils().gerarBcrypt("987654"),
                "12345678900",
                PerfilEnum.ROLE_ADMIN,
                empresa.id!!)
        funcionarioRepository.save(admin)

        val funcionario: Funcionario = Funcionario(
                "Funcionario",
                "funcionario@empresa.com",
                SenhaUtils().gerarBcrypt("12345"),
                "44456789012",
                PerfilEnum.ROLE_USUARIO,
                empresa.id!!)
        funcionarioRepository.save(funcionario)

        System.out.println("Empresa ID: " + empresa.id)
        System.out.println("Admin ID: " + admin.empresaId)
        System.out.println("Funcionário ID: " + funcionario.id)
    }
}

fun main(args: Array<String>) {
    runApplication<PontoInteligenteApplication>(*args)

}
