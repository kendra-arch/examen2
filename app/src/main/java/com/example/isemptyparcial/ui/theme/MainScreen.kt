package com.example.isemptyparcial.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.isemptyparcial.MainViewModel
import com.example.isemptyparcial.models.KanyeQuoteResumen

val backgroundColor = Color(0xFF121212)
val surfaceColor = Color(0xFF1E1E1E)
val textColor = Color(0xFF9C27B0)
val primaryColor = Color(0xFFE91E63)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    var showingFavorites by remember { mutableStateOf(false) }

    val favoritosIds by viewModel.favoritosIds.collectAsState()
    val favoritosList by viewModel.favoritosList.collectAsState()
    val kanyeQuote by viewModel.kanyeState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (showingFavorites) "Favoritos" else "Citas",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = primaryColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { showingFavorites = !showingFavorites }) {
                        Icon(
                            imageVector = if (showingFavorites) Icons.Default.Info else Icons.Default.Favorite,
                            contentDescription = if (showingFavorites) "Ver citas" else "Ver favoritos",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor
                ),
                modifier = Modifier.shadow(8.dp)
            )
        }
    ) { innerPadding ->
        if (showingFavorites) {
            FavoritesContent(
                favoritosList = favoritosList,
                onQuitar = { quoteText -> viewModel.alternarFavoritoKanye(quoteText) },
                modifier = Modifier.padding(innerPadding)
            )
        } else {
            QuoteScreenContent(
                quoteText = kanyeQuote?.quote,
                isLoading = isLoading,
                error = error,
                favoritosIds = favoritosIds,
                onFetchQuote = { viewModel.fetchKanyeQuote() },
                onToggleFavorite = { quote -> viewModel.alternarFavoritoKanye(quote) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun QuoteScreenContent(
    quoteText: String?,
    isLoading: Boolean,
    error: String?,
    favoritosIds: Set<Int>,
    onFetchQuote: () -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Icono de corazón para guardar/quitar de favoritos si hay cita generada
        if (quoteText != null) {
            val esFavorito = quoteText.hashCode() in favoritosIds
            IconButton(
                onClick = { onToggleFavorite(quoteText) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (esFavorito) "Quitar de favoritos" else "Agregar a favoritos",
                    tint = if (esFavorito) Color(0xFFE91E63) else Color.Gray,
                    modifier = Modifier.size(40.dp)
                )
            }
        } else {
            Box(modifier = Modifier.height(40.dp))
        }

        // Caja de texto para mostrar la cita
        val textToShow = when {
            isLoading -> "Buscando información..."
            error != null -> error
            quoteText != null -> "\"$quoteText\""
            else -> "Presiona RANDOM"
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp)
                .background(surfaceColor, RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Text(
                    text = textToShow,
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Botón RANDOM
        Button(
            onClick = onFetchQuote,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(8.dp, RoundedCornerShape(12.dp))
        ) {
            Text(
                text = "RANDOM",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }
    }
}

@Composable
fun FavoritesContent(
    favoritosList: List<KanyeQuoteResumen>,
    onQuitar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mis Favoritos",
            color = textColor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (favoritosList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aún no tienes favoritos.\nToca el ícono de corazón para guardar",
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritosList.size) { index ->
                    val favorito = favoritosList[index]
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "\"${favorito.quote}\"",
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                IconButton(onClick = { onQuitar(favorito.quote) }) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Quitar de favoritos",
                                        tint = Color(0xFFE91E63)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
