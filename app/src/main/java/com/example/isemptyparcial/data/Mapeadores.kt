package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.DummyJSON.DummyProductDto
import com.example.isemptyparcial.data.Frankfurter.FrankfurterResponseDto
import com.example.isemptyparcial.data.JSONPlaceholder.PostDto
import com.example.isemptyparcial.data.local.FavoritoEntity
import com.example.isemptyparcial.models.DummyProductResumen
import com.example.isemptyparcial.models.FrankfurterResumen
import com.example.isemptyparcial.models.JSONPlaceholderResumen

// ============================================================================
// GRUPO 1: MAPELADOR 1 (DummyJSON & Frankfurter) - Productos, Precios y Tasas
// ============================================================================
fun DummyProductDto.aResumen(): DummyProductResumen =
    DummyProductResumen(
        id = id,
        title = title,
        price = price,
        category = category,
        imageUrl = images.firstOrNull() ?: ""
    )

fun DummyProductResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(id = id, quote = "Producto: $title | Precio: $$price")

fun FrankfurterResponseDto.aResumen(): FrankfurterResumen =
    FrankfurterResumen(
        base = base,
        date = date,
        rates = rates
    )

fun FrankfurterResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(id = date.hashCode(), quote = "Base: $base | Tasas EUR/BOB: $rates")

// ============================================================================
// GRUPO 1: MAPELADOR 2 (Frankfurter & JSONPlaceholder) - Tasas y Publicaciones
// ============================================================================
fun FrankfurterResponseDto.aResumenMapeadorDos(): FrankfurterResumen =
    FrankfurterResumen(base = base, date = date, rates = rates)

fun FrankfurterResumen.aEntidadMapeadorDos(): FavoritoEntity =
    FavoritoEntity(id = date.hashCode(), quote = "Moneda Base ($base) - Fecha: $date")

fun PostDto.aResumen(): JSONPlaceholderResumen =
    JSONPlaceholderResumen(
        id = id,
        title = title,
        body = body
    )

fun JSONPlaceholderResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(id = id, quote = "Publicación: $title")
