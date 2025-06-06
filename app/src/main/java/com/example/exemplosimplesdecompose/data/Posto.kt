package com.example.exemplosimplesdecompose.data

data class Posto(
    val nome: String,
    val coordenadas: Coordenadas? = null,
    val precoAlcool: Double = 0.0,
    val precoGasolina: Double = 0.0,
    val dataCadastro: String? = null,
){
    // Construtor secundário com coordenadas de Fortaleza
    constructor(nome: String) : this(nome, Coordenadas(41.40338, 2.17403))
}