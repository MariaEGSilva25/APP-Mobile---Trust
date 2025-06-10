import java.io.Serializable

data class Evento(
    val id: String = "",
    val nome: String = "",
    val categoria: String = "",
    val assunto: String = "",
    val descricao: String = "",
    val dataInicio: String = "",
    val horaInicio: String = "",
    val dataTermino: String = "",
    val horaTermino: String = "",
    val local: String = "",
    val cep: String = "",
    val endereco: String = "",
    val complemento: String = "",
    val estado: String = "",
    val cidade: String = "",
    val valor: Double = 0.0,
    val quantidade: Int = 0,
    val quantidadeIngressosDisponiveis: Int = quantidade,
    val quantidadePorCompra: Int = 1,
    val gratuito: Boolean = false,
    val idProdutor: String = ""
): Serializable
