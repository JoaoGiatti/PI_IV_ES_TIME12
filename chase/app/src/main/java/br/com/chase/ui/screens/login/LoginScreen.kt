package br.com.chase.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.R
import br.com.chase.ui.theme.Poppins


@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.handleSignInResult(result.data)
        }
    }

    LaunchedEffect(state.user) {
        if (state.user != null) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.chase_logo_mais_nome),
                contentDescription = "CHASE",
                modifier = Modifier.size(152.dp, 35.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text("(Adicionar Arte)")
            // Arte
            /*Image(
                painter = painterResource(id = R.drawable.arte),
                contentDescription = "",
            )*/

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Bem vindo(a)!",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = SemiBold,
                    fontSize = 24.sp,
                )
            )
            Text(
                text = "Entre para começar a usar",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = SemiBold,
                    fontSize = 22.sp,
                )
            )
            Row(){
                Text(
                    text = "o ",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = SemiBold,
                        fontSize = 22.sp,
                    )
                )
                Text(
                    text = "Chase!",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = Bold,
                        fontSize = 22.sp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFC84066), Color(0xFFE85944))
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            if (!state.isConnected) {
                Text(
                    text = "Sem conexão com a internet ⚠️",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = { launcher.launch(viewModel.getSignInIntent()) },
                enabled = state.isConnected && !state.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, color = Color(0xFF000000)),

            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.icon_google),
                        contentDescription = "Google",
                        modifier = Modifier
                            .size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Entrar com Google",
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                        )
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                }
            }
        }
    }
}
