package br.com.chase.ui.screens.login

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.chase.R
import br.com.chase.ui.components.NoInternetBanner
import br.com.chase.ui.theme.Poppins
import br.com.chase.ui.theme.PrimaryRainbow

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

    Scaffold(
        topBar = {
            if (!state.isConnected) {
                NoInternetBanner()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chase_logo_mais_nome),
                    contentDescription = "CHASE",
                    modifier = Modifier
                        .size(width = 152.dp, height = 35.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 150.dp),
                contentAlignment = Alignment.Center
            ){
                Image(
                    painter = painterResource(R.drawable.arte),
                    contentDescription = "",
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(4.0f))

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

                Row {
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
                                colors = PrimaryRainbow
                            )
                        )
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                Button(
                    onClick = { launcher.launch(viewModel.getSignInIntent()) },
                    enabled = state.isConnected && !state.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onBackground),
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.icon_google),
                            contentDescription = "Google",
                            modifier = Modifier.size(35.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Entrar com Google",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontSize = 16.sp,
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


