package com.example.isemptyparcial.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.isemptyparcial.MainViewModel
import com.example.isemptyparcial.UiState
import com.example.isemptyparcial.data.local.FavoritoEntity
import com.example.isemptyparcial.models.DummyProductResumen
import com.example.isemptyparcial.models.FrankfurterResumen
import com.example.isemptyparcial.models.JSONPlaceholderResumen

val backgroundColor = Color(0xFF121212)
val surfaceColor = Color(0xFF1E1E1E)
val textColor = Color(0xFFE0E0E0)
val primaryColor = Color(0xFFE91E63)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    var currentTab by remember { mutableStateOf(0) } // 0: Productos, 1: Monedas, 2: Posts, 3: Favoritos

    val productsState by viewModel.productsState.collectAsState()
    val ratesState by viewModel.ratesState.collectAsState()
    val postsState by viewModel.postsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val favoritosIds by viewModel.favoritosIds.collectAsState()
    val favoritosList by viewModel.favoritosList.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentTab) {
                            0 -> "Explorador de Productos"
                            1 -> "Tipos de Cambio"
                            2 -> "Publicaciones"
                            else -> "Favoritos Guardados"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = primaryColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = surfaceColor) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0; viewModel.selectProduct(null) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Productos") },
                    label = { Text("Productos") }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1; viewModel.selectProduct(null) },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Monedas") },
                    label = { Text("Monedas") }
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2; viewModel.selectProduct(null) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Posts") },
                    label = { Text("Posts") }
                )
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3; viewModel.selectProduct(null) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (selectedProduct != null) {
                ProductDetailScreen(
                    product = selectedProduct!!,
                    isFavorite = selectedProduct!!.id in favoritosIds,
                    onToggleFavorite = { viewModel.toggleFavorite(selectedProduct!!) },
                    onBack = { viewModel.selectProduct(null) }
                )
            } else {
                when (currentTab) {
                    0 -> ProductsTab(
                        productsState = productsState,
                        searchQuery = searchQuery,
                        onSearchChange = { viewModel.updateSearchQuery(it) },
                        favoritosIds = favoritosIds,
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onSelectProduct = { viewModel.selectProduct(it) },
                        onRetry = { viewModel.retryProducts() }
                    )
                    1 -> RatesTab(
                        ratesState = ratesState,
                        onRetry = { viewModel.retryRates() }
                    )
                    2 -> PostsTab(
                        postsState = postsState,
                        onRetry = { viewModel.retryPosts() }
                    )
                    3 -> FavoritesTab(
                        favoritosList = favoritosList,
                        onRemove = { viewModel.removeFavorite(it) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsTab(
    productsState: UiState<List<DummyProductResumen>>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    favoritosIds: Set<Int>,
    onToggleFavorite: (DummyProductResumen) -> Unit,
    onSelectProduct: (DummyProductResumen) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Buscar productos...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (productsState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is UiState.Error -> {
                ErrorView(message = productsState.message, onRetry = onRetry)
            }
            is UiState.Success -> {
                val allProducts = productsState.data
                val filteredProducts by remember(allProducts, searchQuery) {
                    derivedStateOf {
                        if (searchQuery.isBlank()) allProducts
                        else allProducts.filter { it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true) }
                    }
                }

                if (filteredProducts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontraron productos", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredProducts, key = { it.id }) { product ->
                            val isFav = product.id in favoritosIds
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectProduct(product) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = surfaceColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = product.imageUrl,
                                        contentDescription = product.title,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.White)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = product.title, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(text = "Categoría: ${product.category}", color = Color.Gray, fontSize = 12.sp)
                                        Text(text = "$ ${product.price}", color = primaryColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    }
                                    IconButton(onClick = { onToggleFavorite(product) }) {
                                        Icon(
                                            imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorito",
                                            tint = primaryColor
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
}

@Composable
fun ProductDetailScreen(
    product: DummyProductResumen,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar", tint = textColor)
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = primaryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = product.title, color = textColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Categoría: ${product.category}", color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Precio: $ ${product.price}", color = primaryColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
fun RatesTab(
    ratesState: UiState<FrankfurterResumen>,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (ratesState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is UiState.Error -> {
                ErrorView(message = ratesState.message, onRetry = onRetry)
            }
            is UiState.Success -> {
                val rates = ratesState.data
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Tipos de Cambio (Base: ${rates.base})", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("Fecha: ${rates.date}", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            rates.rates.entries.forEach { entry ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = entry.key, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(text = entry.value.toString(), color = primaryColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                HorizontalDivider(color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostsTab(
    postsState: UiState<List<JSONPlaceholderResumen>>,
    onRetry: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when (postsState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is UiState.Error -> {
                ErrorView(message = postsState.message, onRetry = onRetry)
            }
            is UiState.Success -> {
                val posts = postsState.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(posts, key = { it.id }) { post ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = surfaceColor)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = post.title, color = textColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = post.body, color = Color.Gray, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoritesTab(
    favoritosList: List<FavoritoEntity>,
    onRemove: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Mis Favoritos Guardados", color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        if (favoritosList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sin elementos guardados", color = Color.Gray, fontSize = 16.sp, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritosList, key = { it.id }) { fav ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = surfaceColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = fav.quote, color = textColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                Text(text = "Guardado: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(fav.agregadoEn))}", color = Color.Gray, fontSize = 10.sp)
                            }
                            IconButton(onClick = { onRemove(fav.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = primaryColor)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = Color.Red, textAlign = TextAlign.Center, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text("Reintentar", color = Color.White)
        }
    }
}
