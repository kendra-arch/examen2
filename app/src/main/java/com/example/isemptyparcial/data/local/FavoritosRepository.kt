package com.example.isemptyparcial.data.local

import com.example.isemptyparcial.data.aEntidad
import com.example.isemptyparcial.models.DummyProductResumen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritosRepository(private val dao: FavoritoDao) {
    fun observarFavoritos(): Flow<List<FavoritoEntity>> =
        dao.observarFavoritos()

    fun observarIds(): Flow<Set<Int>> =
        dao.observarIdsFavoritos().map { ids -> ids.toSet() }

    suspend fun agregar(producto: DummyProductResumen) = withContext(Dispatchers.IO) {
        dao.insertar(producto.aEntidad())
    }

    suspend fun quitar(id: Int) = withContext(Dispatchers.IO) {
        dao.eliminarPorId(id)
    }
}
