const val MENSAGEM_OPCAO_INVALIDA = "Opcao invalida. Por favor, tente novamente."
const val MENSAGEM_SAIR = "A sair..."
const val MENSAGEM_TAMANHO_INVALIDO = "Tamanho do tabuleiro invalido"
const val MENSAGEM_GRAVAR_INDISPONIVEL = "Funcionalidade Gravar nao esta disponivel"
const val MENSAGEM_NOME_INVALIDO = "Nome de jogador invalido"
const val MENSAGEM_INTRODUZA_FICHEIRO = "Introduza o nome do ficheiro (ex: jogo.txt)"
const val SIMBOLO_JOGADOR = "\u001B[31m\u03D9\u001B[0m"
const val SIMBOLO_COMPUTADOR = "\u001B[34m\u03D9\u001B[0m"
const val BORDA_LATERAL = "\u2551"
const val MENSAGEM_COLUNA_INVALIDA = "Coluna invalida"
const val MENSAGEM_EXPLODIR_INDISPONIVEL = "Funcionalidade Explodir nao esta disponivel"
const val MENSAGEM_COLUNA_VAZIA = "Coluna vazia"
const val MENSAGEM_CONTINUAR_EXPLOSAO = "Prima enter para continuar. O computador ira agora explodir um dos seus baloes"
const val MENSAGEM_NUMERO_INVALIDO = "Numero invalido"
const val PERDEU_GANHOU_COMPUTADOR = "Perdeu! Ganhou o Computador."
const val EMPATE = "Empate!"
const val MENSAGEM_TABULEIRO_BASE = "Tabuleiro "
const val SEPARADOR_X = "X"
const val SEPARADOR_X_PEQUENO = "x"
const val MENSAGEM_JOGADOR_VERMELHO = ": \u001B[31m\u03D9\u001B[0m"

//https://www.youtube.com/watch?v=2uHyM0PLPhg - VIDEO

fun main() {
    var exibirBoasVindas = true
    var jogoIniciado = false
    var tabuleiro: Array<Array<String?>>? = null
    var nomeJogador1 = ""
    var mostrarMenu = true


    while (true) {
        if (exibirBoasVindas) {
            exibirMensagemBoasVindas()
            exibirBoasVindas = false
        }

        if (mostrarMenu) {
            exibirMenu()
        }

        val opcaoEscolhida = readlnOrNull()
        mostrarMenu = true

        when {
            !ehOpcaoValida(opcaoEscolhida) -> {
                println(MENSAGEM_OPCAO_INVALIDA)
                mostrarMenu = false
            }
            opcaoEscolhida == "0" -> {
                println(MENSAGEM_SAIR)
                return
            }
            opcaoEscolhida == "1" -> {
                val resultado = iniciarNovoJogo()
                tabuleiro = resultado.first
                nomeJogador1 = resultado.second
                jogoIniciado = true
            }
            opcaoEscolhida == "2" -> {
                if (!jogoIniciado || tabuleiro == null) {
                    println(MENSAGEM_GRAVAR_INDISPONIVEL)
                    mostrarMenu = false
                } else {
                    processarGravacao(tabuleiro, nomeJogador1)
                    mostrarMenu = false
                }
            }
            opcaoEscolhida == "3" -> {
                val resultado = processarLeitura()
                if (resultado != null) {
                    tabuleiro = resultado.second
                    nomeJogador1 = resultado.first
                    jogoIniciado = true
                }
            }
        }
    }
}


fun exibirMensagemBoasVindas() {
    println()
    println("Bem-vindo ao jogo \"4 Baloes em Linha\"!")
    println()
}

fun exibirMenu() {
    println("1. Novo Jogo")
    println("2. Gravar Jogo")
    println("3. Ler Jogo")
    println("0. Sair")
    println()
}

fun processarGravacao(tabuleiro: Array<Array<String?>>, nomeJogador1: String) {
    println(MENSAGEM_INTRODUZA_FICHEIRO)
    val nomeFicheiro = readlnOrNull() ?: ""
    gravaJogo(nomeFicheiro, tabuleiro, nomeJogador1)
}

fun processarLeitura(): Pair<String, Array<Array<String?>>>? {
    println(MENSAGEM_INTRODUZA_FICHEIRO)
    val nomeFicheiro = readlnOrNull() ?: return null
    val resultado = leJogo(nomeFicheiro)

    val (nomeJogador1, tabuleiro) = resultado

    println("Tabuleiro ${tabuleiro.size}x${tabuleiro[0].size} lido com sucesso!")
    println(criaTabuleiro(tabuleiro))
    println()
    println("$nomeJogador1: $SIMBOLO_JOGADOR")
    println("Tabuleiro ${tabuleiro.size}X${tabuleiro[0].size}")

    jogoLoop(tabuleiro, tabuleiro.size, tabuleiro[0].size, nomeJogador1)

    return resultado
}






fun ehOpcaoValida(opcao: String?): Boolean {
    return opcao == "0" || opcao == "1" || opcao == "2" || opcao == "3"
}
fun iniciarNovoJogo(): Pair<Array<Array<String?>>, String> {
    var numeroDeLinhas: Int
    var numeroDeColunas: Int
    var tabuleiroValido = false




    while (!tabuleiroValido) {
        println("Numero de linhas:")
        numeroDeLinhas = lerNumero()


        if (numeroDeLinhas <= 0) {
            println(MENSAGEM_NUMERO_INVALIDO)
        } else if (numeroDeLinhas != 5 && numeroDeLinhas != 6 && numeroDeLinhas != 7) {
            println(MENSAGEM_TAMANHO_INVALIDO)
        } else {
            var colunaValida = false
            while (!colunaValida) {
                println("Numero de colunas:")
                numeroDeColunas = lerNumero()

                if (numeroDeColunas <= 0) {
                    println(MENSAGEM_NUMERO_INVALIDO)
                } else if (numeroDeColunas != 6 && numeroDeColunas != 7 && numeroDeColunas != 8) {
                    println(MENSAGEM_TAMANHO_INVALIDO)
                    tabuleiroValido = false
                    colunaValida = true
                } else {
                    tabuleiroValido = validaTabuleiro(numeroDeLinhas, numeroDeColunas)

                    if (!tabuleiroValido) {
                        println(MENSAGEM_TAMANHO_INVALIDO)
                        colunaValida = true
                    } else {
                        val nomeJogador1 = obterNomeJogador(1)
                        val tabuleiroDoJogo = criaTabuleiroVazio(numeroDeLinhas, numeroDeColunas)

                        println(criaTabuleiro(tabuleiroDoJogo))
                        println()
                        println(nomeJogador1 + MENSAGEM_JOGADOR_VERMELHO)
                        println(MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X + numeroDeColunas)


                        return jogoLoop(tabuleiroDoJogo, numeroDeLinhas, numeroDeColunas, nomeJogador1)
                    }
                }
            }
        }
    }

    return Pair(arrayOf(), "")
}



fun jogoLoop(tabuleiro: Array<Array<String?>>, numeroDeLinhas: Int, numeroDeColunas: Int, nomeJogador1: String): Pair<Array<Array<String?>>, String> {
    val intervaloColuna = calculaIntervaloColuna(numeroDeColunas)

    while (true) {
        println("Coluna? ($intervaloColuna):")
        val comando = readlnOrNull()

        if (!comando.isNullOrEmpty()) {
            val resultado = processarComando(tabuleiro, numeroDeLinhas, numeroDeColunas, nomeJogador1, comando)
            if (resultado != null) return resultado
        }
    }
}

fun processarComando(
    tabuleiro: Array<Array<String?>>,
    numeroDeLinhas: Int,
    numeroDeColunas: Int,
    nomeJogador1: String,
    comando: String
): Pair<Array<Array<String?>>, String>? {
    when {
        comando == "Sair" -> {
            println()
            return Pair(tabuleiro, nomeJogador1)
        }
        comando == "Gravar" -> {
            println("Introduza o nome do ficheiro (ex: jogo.txt)")
            val nomeFicheiro = readlnOrNull() ?: ""
            gravaJogo(nomeFicheiro, tabuleiro, nomeJogador1)
            println()
            return Pair(tabuleiro, nomeJogador1)
        }
        comando.length == 10 && comando[0] == 'E' && comando[1] == 'x' &&
                comando[2] == 'p' && comando[3] == 'l' &&
                comando[4] == 'o' && comando[5] == 'd' && comando[6] == 'i' &&
                comando[7] == 'r' && comando[8] == ' ' &&
                comando[9] in 'A'..'H' -> {
            val colunaEscolhida = comando[9]
            jogadaExplosaoJogador(tabuleiro, colunaEscolhida, nomeJogador1)
        }
        else -> {
            val colunaIndex = processaColuna(numeroDeColunas, comando)
            if (colunaIndex == null) {
                println(MENSAGEM_COLUNA_INVALIDA)
            } else {
                if (colocaBalao(tabuleiro, colunaIndex, true)) {
                    println("Coluna escolhida: $comando")
                    println(criaTabuleiro(tabuleiro))

                    if (ganhouJogo(tabuleiro)) {
                        println()
                        println("Parabens $nomeJogador1! Ganhou!")
                        println()
                        return Pair(tabuleiro, MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X_PEQUENO + numeroDeColunas)
                    }

                    if (eEmpate(tabuleiro)) {
                        println(EMPATE)
                        return Pair(tabuleiro, MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X_PEQUENO + numeroDeColunas)
                    }

                    val proximaColunaIndex = jogadaNormalComputador(tabuleiro)

                    if (proximaColunaIndex != -1) {
                        val letraColuna = ('A' + proximaColunaIndex).toChar()
                        println()
                        println("Computador: \u001B[34m\u03D9\u001B[0m")
                        println(MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X + numeroDeColunas)
                        println("Coluna escolhida: $letraColuna")
                        println(criaTabuleiro(tabuleiro))
                        println()

                        if (ganhouJogo(tabuleiro)) {
                            println(PERDEU_GANHOU_COMPUTADOR)
                            println()
                            return Pair(tabuleiro, MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X_PEQUENO + numeroDeColunas)
                        } else {
                            println(nomeJogador1 + MENSAGEM_JOGADOR_VERMELHO)
                            println(MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X + numeroDeColunas)
                        }

                        if (eEmpate(tabuleiro)) {
                            println()
                            println(EMPATE)
                            println()
                            return Pair(tabuleiro, MENSAGEM_TABULEIRO_BASE + numeroDeLinhas + SEPARADOR_X_PEQUENO + numeroDeColunas)
                        }
                    }
                }
            }
        }
    }
    return null
}





fun obterNomeJogador(numeroJogador: Int): String {
    var nomeValido = false
    var nomeJogador = ""
    while (!nomeValido) {
        println("Nome do jogador $numeroJogador:")
        nomeJogador = readlnOrNull() ?: ""
        nomeValido = nomeValido(nomeJogador)
        if (!nomeValido) {
            println(MENSAGEM_NOME_INVALIDO)
        }
    }
    return nomeJogador
}


fun nomeValido(nome: String): Boolean {
    val tamanhoNome = nome.length
    if (tamanhoNome < 3 || tamanhoNome > 12) return false
    var indiceIteracao = 0
    while (indiceIteracao < tamanhoNome) {
        if (nome[indiceIteracao] == ' ') return false
        indiceIteracao++
    }
    return true
}
fun processaColuna(numeroDeColunas: Int, colunaEscolhida: String?): Int? {
    if (colunaEscolhida.isNullOrEmpty() || colunaEscolhida.length != 1) {
        return null
    }

    val letraColuna = colunaEscolhida[0]
    val intervaloValido = 'A'..<('A' + numeroDeColunas)

    if (letraColuna !in intervaloValido) {
        return null
    }

    return letraColuna - 'A'
}

fun calculaIntervaloColuna(numeroDeColunas: Int): String {
    return when (numeroDeColunas) {
        6 -> "A..F"
        7 -> "A..G"
        8 -> "A..H"
        else -> ""
    }
}

fun lerNumero(): Int {
    val entradaNumero = readlnOrNull()
    if (entradaNumero == null || entradaNumero == "") return -1

    var ehNumeroValido = true
    var indiceIteracaoLinhas = 0

    while (indiceIteracaoLinhas < entradaNumero.length && ehNumeroValido) {
        if (entradaNumero[indiceIteracaoLinhas] < '0' || entradaNumero[indiceIteracaoLinhas] > '9') {
            ehNumeroValido = false
        }
        indiceIteracaoLinhas++
    }

    return if (ehNumeroValido) entradaNumero.toInt() else -1
}


fun validaTabuleiro(numeroDeLinhas: Int, numeroDeColunas: Int): Boolean {
    return (numeroDeLinhas == 5 && numeroDeColunas == 6) ||
            (numeroDeLinhas == 6 && numeroDeColunas == 7) ||
            (numeroDeLinhas == 7 && numeroDeColunas == 8)
}



fun criaTopoTabuleiro(numeroDeColunas: Int): String {
    val bordaSuperior = "\u2554"
    val bordaHorizontal = "\u2550"
    val bordaFinal = "\u2557"
    var topo = bordaSuperior

    for (i in 0..<numeroDeColunas) {
        for (j in 0..2) {
            topo += bordaHorizontal
        }
        if (i < numeroDeColunas - 1) {
            topo += bordaHorizontal
        }
    }
    topo += bordaFinal
    return topo
}


fun criaCorpoTabuleiro(numeroDeLinhas: Int, numeroDeColunas: Int): String {
    val bordaLateral = BORDA_LATERAL
    val espaco = "   "
    val separador = "|"
    var corpo = ""
    for (i in 0..<numeroDeLinhas) {
        var linha = bordaLateral
        for (j in 0..<numeroDeColunas) {
            linha += espaco
            if (j < numeroDeColunas - 1) {
                linha += separador
            }
        }
        linha += bordaLateral
        corpo += linha
        if (i < numeroDeLinhas - 1) {
            corpo += "\n"
        }
    }
    return corpo
}


fun criaLegendaHorizontal(numeroDeColunas: Int): String {
    val margem = " "
    val separador = "|"
    var legenda = margem
    var letraAtual = 'A'
    var indiceIteracaoLegenda = 0
    while (indiceIteracaoLegenda < numeroDeColunas) {
        legenda += " $letraAtual "
        if (indiceIteracaoLegenda < numeroDeColunas - 1) {
            legenda += separador
        }
        letraAtual++
        indiceIteracaoLegenda++
    }
    legenda += " "
    return legenda
}


fun jogadaNormalComputador(tabuleiro: Array<Array<String?>>): Int {
    val numeroDeColunas = tabuleiro[0].size
    val numeroDeLinhas = tabuleiro.size


    for (linha in 0..<numeroDeLinhas) {
        for (coluna in 0..<numeroDeColunas) {
            if (tabuleiro[linha][coluna] == null) {
                tabuleiro[linha][coluna] = SIMBOLO_COMPUTADOR
                return coluna
            }
        }
    }

    return -1
}


fun eEmpate(tabuleiro: Array<Array<String?>>): Boolean {
    for (linha in tabuleiro) {
        for (celula in linha) {
            if (celula.isNullOrEmpty()) {
                return false
            }
        }
    }
    return true
}



fun ganhouJogo(tabuleiro: Array<Array<String?>>): Boolean {
    return eVitoriaHorizontal(tabuleiro) || eVitoriaVertical(tabuleiro) || eVitoriaDiagonal(tabuleiro)
}

fun explodeBalao(tabuleiro: Array<Array<String?>>, posicao: Pair<Int, Int>): Boolean {
    val linha = posicao.first
    val coluna = posicao.second

    if (linha < 0 || linha >= tabuleiro.size || coluna < 0 || coluna >= tabuleiro[0].size || tabuleiro[linha][coluna] == null) {
        return false
    }

    tabuleiro[linha][coluna] = null

    for (i in linha + 1..<tabuleiro.size) {
        tabuleiro[i - 1][coluna] = tabuleiro[i][coluna]
        tabuleiro[i][coluna] = null
    }

    return true
}



fun jogadaExplosaoJogador(tabuleiro: Array<Array<String?>>, colunaEscolhida: Char, nomeJogador1: String) {
    val coluna = colunaEscolhida - 'A'

    if (coluna < 0 || coluna >= tabuleiro[0].size) {
        println(MENSAGEM_COLUNA_INVALIDA)
        return
    }

    val totalBaloes = contaBaloesTabuleiro(tabuleiro)

    if (totalBaloes < 2) {
        println(MENSAGEM_EXPLODIR_INDISPONIVEL)
        return
    }

    var balaoExplodido = false
    var indiceLinha = 0

    for (linha in tabuleiro) {
        val celula = linha[coluna]
        if (celula != null) {
            if (explodeBalao(tabuleiro, Pair(indiceLinha, coluna))) {
                balaoExplodido = true
            }
        }
        indiceLinha++
    }

    if (balaoExplodido) {
        println("Balao $colunaEscolhida explodido!")
        println(criaTabuleiro(tabuleiro))

        println()
        println(MENSAGEM_CONTINUAR_EXPLOSAO)
        readlnOrNull()

        val (linhaComputador, colunaComputador) = jogadaExplodirComputador(tabuleiro)

        if (linhaComputador != -1 && colunaComputador != -1) {
            println("Balao ${('A' + colunaComputador)},${linhaComputador + 1} explodido pelo Computador!")
            println(criaTabuleiro(tabuleiro))

            println()
            val numeroDeLinhas = tabuleiro.size
            val numeroDeColunas = tabuleiro[0].size

            if (ganhouJogo(tabuleiro)) {
                println(PERDEU_GANHOU_COMPUTADOR)
                return
            } else{
                println(nomeJogador1 + MENSAGEM_JOGADOR_VERMELHO)
                println("Tabuleiro ${numeroDeLinhas}X${numeroDeColunas}")
            }

        }
    } else {
        println(MENSAGEM_COLUNA_VAZIA)
    }
}





fun jogadaExplodirComputador(tabuleiro: Array<Array<String?>>): Pair<Int, Int> {
    val numeroDeColunas = tabuleiro[0].size
    val numeroDeLinhas = tabuleiro.size

    for (linha in 0..(numeroDeLinhas - 1)) {
        for (coluna in 0..(numeroDeColunas - 3)) {
            if (tabuleiro[linha][coluna] == SIMBOLO_JOGADOR &&
                tabuleiro[linha][coluna + 1] == SIMBOLO_JOGADOR &&
                tabuleiro[linha][coluna + 2] == SIMBOLO_JOGADOR) {
                explodeBalao(tabuleiro, Pair(linha, coluna))
                return Pair(linha, coluna)
            }
        }
    }

    for (coluna in 0..(numeroDeColunas - 1)) {
        for (linha in 0..(numeroDeLinhas - 3)) {
            if (tabuleiro[linha][coluna] == SIMBOLO_JOGADOR &&
                tabuleiro[linha + 1][coluna] == SIMBOLO_JOGADOR &&
                tabuleiro[linha + 2][coluna] == SIMBOLO_JOGADOR) {
                explodeBalao(tabuleiro, Pair(linha, coluna))
                return Pair(linha, coluna)
            }
        }
    }

    var maiorQuantidade = -1
    var colunaMaisDireita = -1

    for (coluna in 0..(numeroDeColunas - 1)) {
        val totalBaloes = contaBaloesColuna(tabuleiro, coluna)

        if (totalBaloes > maiorQuantidade || (totalBaloes == maiorQuantidade && coluna > colunaMaisDireita)) {
            maiorQuantidade = totalBaloes
            colunaMaisDireita = coluna
        }

    }

    for (linha in 0..(numeroDeLinhas - 1)) {
        if (tabuleiro[linha][colunaMaisDireita] != null) {
            explodeBalao(tabuleiro, Pair(linha, colunaMaisDireita))
            return Pair(linha, colunaMaisDireita)
        }
    }


    return Pair(-1, -1)
}


fun contaBaloesTabuleiro(tabuleiro: Array<Array<String?>>): Int {
    var contador = 0

    for (linha in tabuleiro) {
        for (celula in linha) {
            if (celula != null) {
                contador++
            }
        }
    }

    return contador
}

fun leJogo(nomeFicheiro: String): Pair<String, Array<Array<String?>>> {
    val ficheiro = java.io.File(nomeFicheiro)
    val linhas = ficheiro.readLines()

    val nomeJogador = linhas[0]

    val tabuleiro = Array(linhas.size - 1) { Array<String?>(linhas[1].split(",").size) { null } }

    var indiceLinha = 1
    while (indiceLinha < linhas.size) {
        val elementos = linhas[indiceLinha].split(",")
        var indiceColuna = 0
        while (indiceColuna < elementos.size) {
            tabuleiro[indiceLinha - 1][indiceColuna] = when (elementos[indiceColuna]) {
                "H" -> SIMBOLO_JOGADOR
                "C" -> SIMBOLO_COMPUTADOR
                else -> null
            }
            indiceColuna++
        }
        indiceLinha++
    }

    return Pair(nomeJogador, tabuleiro)
}


fun gravaJogo(nomeFicheiro: String, tabuleiro: Array<Array<String?>>, nomeJogador: String) {
    val ficheiro = java.io.File(nomeFicheiro)
    val filePrinter = ficheiro.printWriter()

    filePrinter.println(nomeJogador)

    for (linha in tabuleiro) {
        var primeiraColuna = true
        for (celula in linha) {
            if (!primeiraColuna) {
                filePrinter.print(",")
            }
            val celulaFormatada = when (celula) {
                SIMBOLO_JOGADOR -> "H"
                SIMBOLO_COMPUTADOR -> "C"
                else -> ""
            }
            filePrinter.print(celulaFormatada)
            primeiraColuna = false
        }
        filePrinter.println()
    }

    filePrinter.close()

    val linhas = tabuleiro.size
    val colunas = if (linhas > 0) tabuleiro[0].size else 0
    println("Tabuleiro ${linhas}x${colunas} gravado com sucesso")

}


fun colocaBalao(tabuleiro: Array<Array<String?>>, coluna: Int, ehBalaoVermelho: Boolean): Boolean {
    val numeroDeLinhas = tabuleiro.size
    for (linha in 0..<numeroDeLinhas) {
        if (tabuleiro[linha][coluna] == null) {
            tabuleiro[linha][coluna] = if (ehBalaoVermelho) {
                SIMBOLO_JOGADOR
            } else {
                SIMBOLO_COMPUTADOR
            }
            return true
        }
    }
    return false
}

fun eVitoriaHorizontal(tabuleiro: Array<Array<String?>>): Boolean {
    for (i in 0..<tabuleiro.size) {
        var counter = 1
        for (j in 1..<tabuleiro[i].size) {
            if (tabuleiro[i][j] != null && tabuleiro[i][j] == tabuleiro[i][j - 1]) {
                counter++
                if (counter == 4) {
                    return true
                }
            } else {
                counter = 1
            }
        }
    }
    return false
}

fun eVitoriaVertical(tabuleiro: Array<Array<String?>>): Boolean {
    for (j in 0..<tabuleiro[0].size) {
        var counter = 1
        for (i in 1..<tabuleiro.size) {
            if (tabuleiro[i][j] != null && tabuleiro[i][j] == tabuleiro[i - 1][j]) {
                counter++
                if (counter == 4) {
                    return true
                }
            } else {
                counter = 1
            }
        }
    }
    return false
}

fun eVitoriaDiagonal(tabuleiro: Array<Array<String?>>): Boolean {
    val numeroDeLinhas = tabuleiro.size
    val numeroDeColunas = tabuleiro[0].size

    for (linha in 0..numeroDeLinhas - 4) {
        for (coluna in 0..numeroDeColunas - 4) {
            val valor = tabuleiro[linha][coluna]
            if (valor != null &&
                valor == tabuleiro[linha + 1][coluna + 1] &&
                valor == tabuleiro[linha + 2][coluna + 2] &&
                valor == tabuleiro[linha + 3][coluna + 3]) {
                return true
            }
        }
    }

    for (linha in 0..numeroDeLinhas - 4) {
        for (coluna in 3..<numeroDeColunas) {
            val valor = tabuleiro[linha][coluna]
            if (valor != null &&
                valor == tabuleiro[linha + 1][coluna - 1] &&
                valor == tabuleiro[linha + 2][coluna - 2] &&
                valor == tabuleiro[linha + 3][coluna - 3]) {
                return true
            }
        }
    }

    return false
}

fun criaTabuleiroVazio(linhas: Int, colunas: Int): Array<Array<String?>> {
    return Array(linhas) { arrayOfNulls<String?>(colunas) }
}

fun criaTabuleiro(tabuleiro: Array<Array<String?>>, mostraLegenda: Boolean = true): String {
    val numeroDeLinhas = tabuleiro.size
    val numeroDeColunas = if (tabuleiro.isNotEmpty()) tabuleiro[0].size else 0


    var bordaSuperior = "\u2554"
    for (coluna in 0..<numeroDeColunas) {
        bordaSuperior += "\u2550\u2550\u2550"
        if (coluna < numeroDeColunas - 1) {
            bordaSuperior += "\u2550"
        }
    }
    bordaSuperior += "\u2557"


    var corpo = ""
    for (linha in 0..<numeroDeLinhas) {
        var linhaConteudo = BORDA_LATERAL
        for (coluna in 0..<numeroDeColunas) {
            val celula = tabuleiro[linha][coluna] ?: " "
            linhaConteudo += " $celula "
            if (coluna < numeroDeColunas - 1) {
                linhaConteudo += "|"
            }
        }
        linhaConteudo += BORDA_LATERAL
        corpo += linhaConteudo
        if (linha < numeroDeLinhas - 1) {
            corpo += "\n"
        }
    }

    var legenda = ""
    if (mostraLegenda) {
        legenda = " "
        var letraAtual = 'A'
        for (coluna in 0..<numeroDeColunas) {
            legenda += " $letraAtual "
            letraAtual++
            if (coluna < numeroDeColunas - 1) {
                legenda += "|"
            }
        }
        legenda += " "
    }

    return if (mostraLegenda) {
        "$bordaSuperior\n$corpo\n$legenda"
    } else {
        "$bordaSuperior\n$corpo"
    }
}



fun contaBaloesLinha(tabuleiro: Array<Array<String?>>, linha: Int): Int {
    var contador = 0

    for (coluna in tabuleiro.indices) {
        val celula = tabuleiro[linha][coluna]
        if (celula != null) {
            contador++
        }
    }
    return contador
}


fun contaBaloesColuna(tabuleiro: Array<Array<String?>>, coluna: Int): Int {
    var contador = 0

    for (linha in 0..<tabuleiro.size) {
        val celula = tabuleiro[linha][coluna]
        if (celula != null) {
            contador++
        }
    }
    return contador
}
