package com.example.isemptyparcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isemptyparcial.data.DummyJSON.DummyProductRepository
import com.example.isemptyparcial.data.Frankfurter.FrankfurterRepository
import com.example.isemptyparcial.data.JSONPlaceholder.JSONPlaceholderRepository
import com.example.isemptyparcial.data.aResumen
import com.example.isemptyparcial.data.local.FavoritoEntity
import com.example.isemptyparcial.data.local.FavoritosRepository
import com.example.isemptyparcial.data.local.KanyeWestDatabase
import com.example.isemptyparcial.models.DummyProductResumen
import com.example.isemptyparcial.models.FrankfurterResumen
import com.example.isemptyparcial.models.JSONPlaceholderResumen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository = DummyProductRepository()
    private val frankfurterRepository = FrankfurterRepository()
    private val postRepository = JSONPlaceholderRepository()
    private val favoritosRepository = FavoritosRepository(KanyeWestDatabase.obtener(application).favoritoDao())

    // UI States for the 3 APIs
    private val _productsState = MutableStateFlow<UiState<List<DummyProductResumen>>>(UiState.Loading)
    val productsState: StateFlow<UiState<List<DummyProductResumen>>> = _productsState.asStateFlow()

    private val _ratesState = MutableStateFlow<UiState<FrankfurterResumen>>(UiState.Loading)
    val ratesState: StateFlow<UiState<FrankfurterResumen>> = _ratesState.asStateFlow()

    private val _postsState = MutableStateFlow<UiState<List<JSONPlaceholderResumen>>>(UiState.Loading)
    val postsState: StateFlow<UiState<List<JSONPlaceholderResumen>>> = _postsState.asStateFlow()

    // Search Query State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Selected Product for Detail Screen
    private val _selectedProduct = MutableStateFlow<DummyProductResumen?>(null)
    val selectedProduct: StateFlow<DummyProductResumen?> = _selectedProduct.asStateFlow()

    fun selectProduct(product: DummyProductResumen?) {
        _selectedProduct.value = product
    }

    // Favorites
    val favoritosIds: StateFlow<Set<Int>> = favoritosRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val favoritosList: StateFlow<List<FavoritoEntity>> = favoritosRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        fetchAll()
    }

    fun fetchAll() {
        fetchProducts()
        fetchRates()
        fetchPosts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            try {
                val response = productRepository.obtenerProductos()
                val list = response.products.map { it.aResumen() }
                _productsState.value = UiState.Success(list)
            } catch (e: Exception) {
                _productsState.value = UiState.Error("Error al cargar productos: ${e.localizedMessage}")
            }
        }
    }

    fun fetchRates() {
        viewModelScope.launch {
            _ratesState.value = UiState.Loading
            try {
                val response = frankfurterRepository.obtenerRates()
                val resumen = response.aResumen()
                _ratesState.value = UiState.Success(resumen)
            } catch (e: Exception) {
                _ratesState.value = UiState.Error("Error al cargar tipos de cambio: ${e.localizedMessage}")
            }
        }
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            try {
                val response = postRepository.obtenerPosts()
                val list = response.map { it.aResumen() }
                _postsState.value = UiState.Success(list)
            } catch (e: Exception) {
                _postsState.value = UiState.Error("Error al cargar publicaciones: ${e.localizedMessage}")
            }
        }
    }

    fun retryProducts() = fetchProducts()
    fun retryRates() = fetchRates()
    fun retryPosts() = fetchPosts()

    fun toggleFavorite(product: DummyProductResumen) {
        val isFav = product.id in favoritosIds.value
        viewModelScope.launch {
            if (isFav) {
                favoritosRepository.quitar(product.id)
            } else {
                favoritosRepository.agregar(product)
            }
        }
    }

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            favoritosRepository.quitar(id)
        }
    }
}
