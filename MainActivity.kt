package com.example.login_home2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clean_energy.CadastroScreen
import com.example.clean_energy.ListarAtividadesScreen
import com.example.clean_energy.atividadesCadastradas
import com.example.login_home2.ui.theme.Login_home2Theme

// Constantes para as rotas
object Screen {
    const val Login = "login"
    const val Principal = "principal"
    const val Desafios = "desafios"
    const val Pontuacao = "pontuacao"
    const val Dicas = "dicas"
    const val Videos = "videos"
    const val Cadastro = "cadastro"
    const val Listaratividades = "listaratividades"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Login_home2Theme {
                val navController = rememberNavController()

                // Scaffold para layout com padding interno
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Cadastro, // Tela inicial configurada para o cadastro
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Definindo as telas e suas respectivas rotas
                        composable(Screen.Login) {
                            LoginPage(navController = navController)
                        }
                        composable(Screen.Principal) {
                            PrincipalScreen(navController = navController)
                        }
                        composable(Screen.Desafios) {
                            DesafiosScreen(navController = navController)
                        }
                        composable(Screen.Pontuacao) {
                            PontuacaoScreen(navController = navController)
                        }
                        composable(Screen.Dicas) {
                            DicasScreen(navController = navController)
                        }
                        composable(Screen.Videos) {
                            VideosScreen(navController = navController)
                        }
                        composable(Screen.Cadastro) {
                            CadastroScreen(navController = navController)
                        }
                        composable(Screen.Listaratividades) {
                            ListarAtividadesScreen(navController = navController, atividades = atividadesCadastradas)
                        }
                    }
                }
            }
        }
    }
}



