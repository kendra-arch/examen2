package com.example.isemptyparcial.data

import com.example.isemptyparcial.data.local.FavoritoEntity
import com.example.isemptyparcial.models.KanyeQuoteResumen

fun FavoritoEntity.aResumen(): KanyeQuoteResumen =
    KanyeQuoteResumen(quote = quote)

fun KanyeQuoteResumen.aEntidad(): FavoritoEntity =
    FavoritoEntity(id = quote.hashCode(), quote = quote)
