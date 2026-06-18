package com.example.hw2.highscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw2.databinding.FragmentScoresTableBinding

/**
 * Top fragment of the high-scores screen: a scrollable table of the ten best
 * scores. Tapping a row asks the shared [HighScoresViewModel] to select it,
 * which moves the map in [ScoresMapFragment].
 */
class ScoresTableFragment : Fragment() {

    private var _binding: FragmentScoresTableBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HighScoresViewModel by activityViewModels()
    private val adapter = ScoresAdapter { score -> viewModel.select(score) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScoresTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerScores.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerScores.adapter = adapter

        viewModel.scores.observe(viewLifecycleOwner) { scores ->
            adapter.submit(scores)
            binding.txtEmpty.visibility = if (scores.isEmpty()) View.VISIBLE else View.GONE
        }
        viewModel.selected.observe(viewLifecycleOwner) { selected ->
            adapter.setSelected(selected?.id ?: -1L)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerScores.adapter = null
        _binding = null
    }
}
