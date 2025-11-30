package br.com.chase.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RouteNameDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var routeName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nome da rota") },
        text = {
            Column {
                Text("Digite um nome para salvar sua rota:")
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.TextField(
                    value = routeName,
                    onValueChange = { routeName = it },
                    singleLine = true,
                    label = { Text("Nome") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (routeName.isNotBlank()) {
                        onConfirm(routeName.trim())
                    }
                }
            ) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
