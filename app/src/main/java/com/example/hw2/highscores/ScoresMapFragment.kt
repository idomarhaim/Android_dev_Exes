package com.example.hw2.highscores

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.hw2.R
import com.example.hw2.data.HighScore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Bottom fragment of the high-scores screen: a map with a marker for every
 * score that has a recorded location. Selecting a row in
 * [ScoresTableFragment] re-centres the camera here.
 */
class ScoresMapFragment : SupportMapFragment(), OnMapReadyCallback {

    private val viewModel: HighScoresViewModel by activityViewModels()
    private var map: GoogleMap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        viewModel.scores.observe(viewLifecycleOwner) { scores ->
            renderMarkers(scores)
        }
        viewModel.selected.observe(viewLifecycleOwner) { selected ->
            selected?.let { focusOn(it) }
        }
    }

    private fun renderMarkers(scores: List<HighScore>) {
        val gmap = map ?: return
        gmap.clear()

        val located = scores.filter { it.hasLocation }
        if (located.isEmpty()) return

        val bounds = LatLngBounds.Builder()
        located.forEach { score ->
            val position = LatLng(score.latitude, score.longitude)
            gmap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(getString(R.string.score_format, score.score))
                    .snippet(
                        getString(R.string.score_details_format, score.distance, score.coins)
                    )
            )?.tag = score.id
            bounds.include(position)
        }

        // Frame all markers (or zoom to the only one). newLatLngBounds needs the
        // map to be laid out, so fall back to a plain zoom if it isn't yet.
        if (located.size == 1) {
            val only = located.first()
            gmap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(LatLng(only.latitude, only.longitude), 13f)
            )
        } else {
            try {
                gmap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), MAP_PADDING_PX))
            } catch (_: IllegalStateException) {
                val first = located.first()
                gmap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(first.latitude, first.longitude), 11f)
                )
            }
        }
    }

    private fun focusOn(score: HighScore) {
        if (!score.hasLocation) return
        val gmap = map ?: return
        gmap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(score.latitude, score.longitude), 14f
            )
        )
    }

    companion object {
        private const val MAP_PADDING_PX = 96
    }
}
