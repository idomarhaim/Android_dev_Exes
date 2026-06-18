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
        scores.filter { it.hasLocation }.forEachIndexed { index, score ->
            gmap.addMarker(
                MarkerOptions()
                    .position(LatLng(score.latitude, score.longitude))
                    .title(getString(R.string.score_format, score.score))
                    .snippet(
                        getString(R.string.score_details_format, score.distance, score.coins)
                    )
            )?.tag = score.id
            if (index == 0) focusOn(score)
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
}
