package com.example.isemptyparcial

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.isemptyparcial.data.kanye.KanyeQuoteDto
import com.example.isemptyparcial.data.kanye.KanyeQuoteRepository
import com.example.isemptyparcial.data.local.FavoritosRepository
import com.example.isemptyparcial.data.local.KanyeWestDatabase
import com.example.isemptyparcial.models.KanyeQuoteResumen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val kanyeRepository = KanyeQuoteRepository()
    private val favoritosRepository = FavoritosRepository(KanyeWestDatabase.obtener(application).favoritoDao())

    val favoritosIds: StateFlow<Set<Int>> = favoritosRepository.observarIds()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    val favoritosList: StateFlow<List<KanyeQuoteResumen>> = favoritosRepository.observarFavoritos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun alternarFavoritoKanye(quoteText: String) {
        val id = quoteText.hashCode()
        val esFavorito = id in favoritosIds.value
        viewModelScope.launch {
            if (esFavorito) {
                favoritosRepository.quitar(id)
            } else {
                favoritosRepository.agregar(KanyeQuoteResumen(quote = quoteText))
            }
        }
    }

    private val _kanyeState = MutableStateFlow<KanyeQuoteDto?>(null)
    val kanyeState: StateFlow<KanyeQuoteDto?> = _kanyeState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchKanyeQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                val quote = kanyeRepository.obtenerCItaKanyeAleatoria()
                _kanyeState.value = quote
            } catch (e: Exception) {
                _errorState.value = "Hubo un error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
