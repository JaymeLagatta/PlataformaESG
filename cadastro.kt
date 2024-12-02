package com.example.clean_energy

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.runtime.Composable

// class para guardar, persistir os dados:
data class Atividade(val nome: String, val descricao: String, val datainicio: String, val datafim: String, val endereco: String, val cep: String, val cpf: String)

// lista gertal para armazenar atividades
val atividadesCadastradas = mutableListOf<Atividade>()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Estados para gerenciar os campos de entrada
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var cep by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var endereco by remember { mutableStateOf("") }
    var mensagemValidacao by remember { mutableStateOf("") }
    var objetivoSelecionado by remember { mutableStateOf<String?>(null) }
    var corDeFundo by remember { mutableStateOf(Color(0xF9FAFB)) }

    // FocusRequester para gerenciar o foco no campo de nome
    val focusRequesterNome = remember { FocusRequester() }

    // Lista de objetivos de desenvolvimento sustentável e suas cores
    val objetivos = mapOf(
        "Erradicação da Pobreza" to Color.Red,
        "Fome Zero" to Color(0xFFB8860B),
        "Saúde e Bem Estar" to Color.Green,
        "Educação de Qualidade" to Color(0xFF8B0000),
        "Igualdade de Gênero" to Color(0xFFFF6347),
        "Água Potável e Saneamento" to Color.Cyan,
        "Energia Limpa e Acessível" to Color.Yellow,
        "Trabalho Decente e Crescimento Econômico" to Color(0xFF800000),
        "Redução das Desigualdades" to Color.Magenta,
        "Ação Contra a Mudança Global do Clima" to Color(0xFF228B22)
    )
    val expanded = remember { mutableStateOf(false) }

    // Solicitar foco no campo de nome após a composição estar pronta
    LaunchedEffect(Unit) {
        focusRequesterNome.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro de Atividades", color = Color(0xFFffffff)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFFFFFFFF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF719c10))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(corDeFundo)
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
            //    Text(
            //        text = "Cadastro de Atividade",
            //        fontSize = 28.sp,
            //        color = Color(0xFF04344d),
            //        modifier = Modifier.padding(bottom = 16.dp),
            //        textAlign = TextAlign.Center
            //    )
                // Caixa de explicação sobre os ODS
            //    Box(
            //        modifier = Modifier
            //            .fillMaxWidth(), // Preenche a largura disponivel
            //        contentAlignment = Alignment.Center
            //    ) {
            //        Text(
            //            text = "Os ODSs contem uma agenda com 17 Objetivos e \n"+
            //                    "169 metas para serem atingidas até 2030!",
            //            fontSize = 14.sp,
            //            color = Color.Gray,
            //            modifier = Modifier.padding(16.dp) // Adiciona um espaçamento interno
            //        )
            //    }
                // Menu suspenso para selecionar um objetivo
                ExposedDropdownMenuBox(
                    expanded = expanded.value,
                    onExpandedChange = { expanded.value = !expanded.value }
                ) {
                    OutlinedTextField(
                        value = objetivoSelecionado ?: "",
                        onValueChange = {},
                        label = { Text("Selecione um Objetivo") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { expanded.value = true },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        objetivos.keys.forEach { objetivo ->
                            DropdownMenuItem(
                                text = { Text(text = objetivo) },
                                onClick = {
                                    objetivoSelecionado = objetivo
                                    corDeFundo = objetivos[objetivo] ?: Color(0xF9FAFB)
                                    expanded.value = false
                                }
                            )
                        }
                    }
                }

                // Campo de entrada para o nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Título da atividade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .focusRequester(focusRequesterNome),
                    colors = TextFieldDefaults.outlinedTextFieldColors()
                )

                // Campo de entrada para a descrição
                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição da atividade") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                //inicio datas
// Estados para gerenciar os campos de data
                var dataInicio by remember { mutableStateOf("") }
                var dataFim by remember { mutableStateOf("") }

                var showDatePickerInicio by remember { mutableStateOf(false) }
                var showDatePickerFim by remember { mutableStateOf(false) }

// Linha com os botões lado a lado
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Botão para selecionar Data de Início
                    Button(
                        onClick = { showDatePickerInicio = true },
                        modifier = Modifier.weight(1f), // Divide espaço igualmente
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe69138))
                    ) {
                        Text(
                            text = if (dataInicio.isNotEmpty()) "Início: $dataInicio" else "Data Início",
                            fontSize = 12.sp, // Reduz o tamanho do texto para ajustar ao botão menor
                            maxLines = 1 // Garante que o texto não quebre
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp)) // Espaçamento entre os botões

                    // Botão para selecionar Data de Fim
                    Button(
                        onClick = { showDatePickerFim = true },
                        modifier = Modifier.weight(1f), // Divide espaço igualmente
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8c5113))
                    ) {
                        Text(
                            text = if (dataFim.isNotEmpty()) "Fim: $dataFim" else "Data Fim",
                            fontSize = 12.sp, // Reduz o tamanho do texto para ajustar ao botão menor
                            maxLines = 1 // Garante que o texto não quebre
                        )
                    }
                }

// DatePickerDialog para Data de Início
                if (showDatePickerInicio) {
                    val datePickerStateInicio = rememberDatePickerState()
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerInicio = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showDatePickerInicio = false
                                val selectedMillis = datePickerStateInicio.selectedDateMillis
                                if (selectedMillis != null) {
                                    val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                        .format(java.util.Date(selectedMillis))
                                    dataInicio = date
                                }
                            }) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerInicio = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerStateInicio)
                    }
                }

// DatePickerDialog para Data de Fim
                if (showDatePickerFim) {
                    val datePickerStateFim = rememberDatePickerState()
                    DatePickerDialog(
                        onDismissRequest = { showDatePickerFim = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showDatePickerFim = false
                                val selectedMillis = datePickerStateFim.selectedDateMillis
                                if (selectedMillis != null) {
                                    val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                                        .format(java.util.Date(selectedMillis))
                                    dataFim = date
                                }
                            }) {
                                Text("Confirmar")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerFim = false }) {
                                Text("Cancelar")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerStateFim)
                    }
                }

// fim datas

                // Campo de CEP
                OutlinedTextField(
                    value = cep,
                    onValueChange = { newCep ->
                        cep = newCep
                        if (newCep.length == 8) {
                            coroutineScope.launch(Dispatchers.IO) {
                                val result = buscarEndereco(newCep)
                                endereco = result ?: "Endereço não encontrado"
                            }
                        }
                    },
                    label = { Text("CEP da atividade") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors()
                )

                // Exibir endereço
                if (endereco.isNotEmpty()) {
                    Text(
                        text = "Endereço: $endereco",
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.Gray
                    )
                }
                // Campo de entrada para o CPF
                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Botão para cadastrar atividade
                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.IO) {
                            val cpfValido = validarCpf(cpf)
                            mensagemValidacao = if (cpfValido) {
                                atividadesCadastradas.add(
                                    Atividade(nome, descricao, dataInicio, dataFim, endereco, cep, cpf)
                                )
                                "Cadastro realizado com sucesso!"
                            } else {
                                "CPF inválido!"
                            }
                            coroutineScope.launch(Dispatchers.Main) {
                                Toast.makeText(context, mensagemValidacao, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF719c10))
                ) {
                    Text("Cadastrar Ação")
                }

                // Mensagem de validação
                if (mensagemValidacao.isNotEmpty()) {
                    Text(
                        text = mensagemValidacao,
                        color = if (mensagemValidacao.contains("sucesso")) Color.Green else Color.Red,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão para visualizar atividades cadastradas
                Button(
                    onClick = { navController.navigate("listarAtividades") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF344808))
                ) {
                    Text("Ver Atividades Cadastradas")
                }
            }
        }
    )
}

// Função para buscar endereço pela API de CEP
fun buscarEndereco(cep: String): String? {
    val url = "https://viacep.com.br/ws/$cep/json/"
    return try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.inputStream.bufferedReader().use {
            val response = it.readText()
            val json = JSONObject(response)
            if (json.has("erro")) null else json.getString("logradouro") + ", " +
                    json.getString("bairro") + ", " + json.getString("localidade") + " - " +
                    json.getString("uf")
        }
    } catch (e: Exception) {
        null
    }
}

// Função para validar CPF
fun validarCpf(cpf: String): Boolean {
    if (cpf.length != 11) return false
    val digitos = cpf.map { it.toString().toInt() }

    // Cálculo dos dois dígitos verificadores
    val digito1 = calcularDigitoVerificador(digitos, 10)
    val digito2 = calcularDigitoVerificador(digitos, 11)

    return digitos[9] == digito1 && digitos[10] == digito2
}

fun calcularDigitoVerificador(digitos: List<Int>, peso: Int): Int {
    val soma = digitos.take(peso - 1).withIndex().sumOf { it.value * (peso - it.index) }
    val resto = soma % 11
    return if (resto < 2) 0 else 11 - resto
}
