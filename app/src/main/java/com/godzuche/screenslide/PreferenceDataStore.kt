package com.godzuche.screenslide

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.godzuche.screenslide.page_transformers.ZoomOutPageTransformer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val TRANSFORMER_PREFERENCES_NAME = "transformer_preferences"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = TRANSFORMER_PREFERENCES_NAME
)

class PreferenceDataStore(context: Context) {

    suspend fun setZoomOutPageTransformer(isZoomOutPageTransformer: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_ZOOM_OUT_PAGE_TRANSFORMER] = isZoomOutPageTransformer
            preferences[IS_DEPTH_PAGE_TRANSFORMER] = !isZoomOutPageTransformer
        }
    }
    suspend fun setDepthPageTransformer(isDepthPageTransformer: Boolean, context: Context) {
        context.dataStore.edit { preferences ->
            preferences[IS_DEPTH_PAGE_TRANSFORMER] = isDepthPageTransformer
            preferences[IS_ZOOM_OUT_PAGE_TRANSFORMER] = !isDepthPageTransformer
        }
    }

        val preferencesFlow: Flow<Preferences> = context.dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }

    companion object {
        val IS_DEPTH_PAGE_TRANSFORMER = booleanPreferencesKey("depth_transform")
        val IS_ZOOM_OUT_PAGE_TRANSFORMER = booleanPreferencesKey("zoom_out_transform")
    }

}
/*

enum class PageTransformer {
    ZOOM_OUT, DEPTH
}*/
