package com.example.exemplosimplesdecompose.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object PostoPreferences {

    private const val PREF_NAME = "postos_pref"
    private const val CHAVE_POSTOS = "lista_de_postos"

    fun salvarPosto(context: Context, novoPosto: Posto) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val listaAtual = recuperarPostos(context).toMutableList()
        listaAtual.add(novoPosto)
        val json = gson.toJson(listaAtual)
        prefs.edit().putString(CHAVE_POSTOS, json).apply()
    }

    fun salvarPostos(context: Context, postos: List<Posto>) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(postos)  // você pode usar Gson ou outra lib JSON
        editor.putString(CHAVE_POSTOS, json)
        editor.apply()
    }

    fun recuperarPostos(context: Context): List<Posto> {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(CHAVE_POSTOS, null)
        return if (json != null) {
            val tipo = object : TypeToken<List<Posto>>() {}.type
            Gson().fromJson(json, tipo)
        } else {
            emptyList()
        }
    }
}
