package com.example.exemplosimplesdecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.exemplosimplesdecompose.ui.theme.ExemploSimplesDeComposeTheme
import com.example.exemplosimplesdecompose.view.AlcoolGasolinaPreco
import com.example.exemplosimplesdecompose.view.DetalhePostoScreen
import com.example.exemplosimplesdecompose.view.EditarPosto
import com.example.exemplosimplesdecompose.view.InputView
import com.example.exemplosimplesdecompose.view.ListaDePostos
import com.example.exemplosimplesdecompose.view.Welcome

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExemploSimplesDeComposeTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainalcgas") {
                    composable("welcome") { Welcome(navController) }
                    composable("input") { InputView(navController) }
                    composable("mainalcgas") { AlcoolGasolinaPreco(navController) }
                    composable("ListaDePostos/{nomeDoPosto}") { backStackEntry ->
                        val posto = backStackEntry.arguments?.getString("nomeDoPosto") ?: ""
                        ListaDePostos(navController, posto)
                    }
                    composable(
                        route = "EditarPosto/{nomePosto}",
                        arguments = listOf(navArgument("nomePosto") { type = NavType.StringType })
                    ) {
                        val nomePosto = it.arguments?.getString("nomePosto")
                        if (nomePosto != null) {
                            EditarPosto(navController, nomePosto)
                        }
                    }
                    composable(
                        route = "DetalhePosto/{nomeDoPosto}",
                        arguments = listOf(navArgument("nomeDoPosto") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val nomeDoPosto = backStackEntry.arguments?.getString("nomeDoPosto") ?: ""
                        DetalhePostoScreen(navController, nomeDoPosto)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExemploSimplesDeComposeTheme {
        Greeting("Android")
    }
}