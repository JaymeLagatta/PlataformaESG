package com.example.clean_energy

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

/**
 * Classe de dados para representar uma Atividade cadastrada (linha 31 da tela cadastro)
 * listar atividades
 *
 * @param navController Controlador de navegação para gerenciar transições entre telas.
 * @param atividades Lista de atividades cadastradas para exibição.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarAtividadesScreen(navController: NavHostController, atividades: List<Atividade>) {
    Scaffold(
        // Barra superior da tela
        topBar = {
            TopAppBar(
                title = { Text("Atividades Cadastradas", color = Color(0xFFFFFFFF)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Botão para voltar à tela anterior
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
        // Conteúdo principal da tela
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Itera sobre a lista de atividades e exibe cada uma em um Card
                items(atividades) { atividade ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Exibe as informações da atividade
                            Text(
                                text = "Atividade: ${atividade.nome}",
                                fontSize = 18.sp,
                                color = Color(0xFF04344d)
                            )
                            Text(
                                text = "Descrição: ${atividade.descricao}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Data Início: ${atividade.datainicio}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Data Fim: ${atividade.datafim}",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "CEP: ${atividade.cep}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Endereço: ${atividade.endereco}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "CPF: ${atividade.cpf}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            // Botão para voltar diretamente para a tela de cadastro
            Button(
                onClick = { navController.navigate("cadastroScreen") }, // Navega para a tela de cadastro
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF04344d))
            ) {
                Text("Voltar para Cadastro", color = Color.White)
            }
        }
    )
}
