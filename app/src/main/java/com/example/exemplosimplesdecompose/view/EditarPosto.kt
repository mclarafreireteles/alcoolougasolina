package com.example.exemplosimplesdecompose.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.exemplosimplesdecompose.data.Posto
import com.example.exemplosimplesdecompose.data.PostoPreferences
import android.widget.Toast
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import java.util.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPosto(
    navController: NavHostController,
    nomeDoPosto: String
) {
    val context = LocalContext.current

    // Recupera a lista atual de postos
    var postos by remember { mutableStateOf(PostoPreferences.recuperarPostos(context)) }
    // Busca o posto que será editado
    val postoOriginal = postos.find { it.nome == nomeDoPosto }

    // Estados para os campos editáveis
    var nome by remember { mutableStateOf(postoOriginal?.nome ?: "") }
    var precoAlcool by remember { mutableStateOf(postoOriginal?.precoAlcool?.toString() ?: "") }
    var precoGasolina by remember { mutableStateOf(postoOriginal?.precoGasolina?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Posto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo Nome
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome do Posto") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo Preço Álcool
            OutlinedTextField(
                value = precoAlcool,
                onValueChange = { precoAlcool = it },
                label = { Text("Preço Álcool") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Campo Preço Gasolina
            OutlinedTextField(
                value = precoGasolina,
                onValueChange = { precoGasolina = it },
                label = { Text("Preço Gasolina") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Validações básicas
                    if (nome.isBlank()) {
                        Toast.makeText(context, "Nome não pode ser vazio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val alcoolVal = precoAlcool.toDoubleOrNull()
                    val gasolinaVal = precoGasolina.toDoubleOrNull()

                    if (alcoolVal == null || gasolinaVal == null) {
                        Toast.makeText(context, "Preços devem ser números válidos", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // Atualiza o posto na lista
                    val index = postos.indexOfFirst { it.nome == nomeDoPosto }
                    if (index >= 0) {
                        val postoAtualizado = postoOriginal?.copy(
                            nome = nome.trim(),
                            precoAlcool = alcoolVal,
                            precoGasolina = gasolinaVal
                        )
                        postoAtualizado?.let {
                            postos = postos.toMutableList().also { list ->
                                list[index] = it
                            }
                            PostoPreferences.salvarPostos(context, postos)
                            Toast.makeText(context, "Posto atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } else {
                        Toast.makeText(context, "Erro ao encontrar o posto para atualizar", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Salvar")
            }
        }
    }
}
