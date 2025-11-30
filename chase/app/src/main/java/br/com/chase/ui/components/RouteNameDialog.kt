package br.com.chase.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.chase.ui.theme.Poppins
import br.com.chase.ui.theme.PrimaryRainbow

@Composable
fun RouteNameDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var routeName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(18.dp),
        title = {
            Text(
                text = "Nome da rota",
                fontFamily = Poppins,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "Digite um nome para salvar sua rota:",
                    fontFamily = Poppins,
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = routeName,
                    onValueChange = { routeName = it },
                    singleLine = true,
                    label = {
                        Text(
                            text = "Nome",
                            fontFamily = Poppins,
                            fontSize = 14.sp
                        )
                    },
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (routeName.isNotBlank()) {
                        onConfirm(routeName.trim())
                    }
                },
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = PrimaryRainbow
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(
                            vertical = 8.dp,
                            horizontal = 35.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Salvar",
                        color = Color.White,
                        fontFamily = Poppins,
                        fontSize = 12.sp
                    )
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(colors = PrimaryRainbow)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Cancelar",
                    fontFamily = Poppins,
                    fontSize = 12.sp
                )
            }
        }
    )
}
