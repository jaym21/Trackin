package dev.jaym21.trackin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.jaym21.trackin.R
import dev.jaym21.trackin.adapter.ISessionRVAdapter
import dev.jaym21.trackin.adapter.SessionRVAdapter
import dev.jaym21.trackin.databinding.FragmentAllSessionsBinding
import dev.jaym21.trackin.model.Session
import dev.jaym21.trackin.viewmodel.MainViewModel

@AndroidEntryPoint
class AllSessionsFragment : Fragment(), ISessionRVAdapter {

    private var _binding: FragmentAllSessionsBinding? = null
    private val binding: FragmentAllSessionsBinding
        get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val sessionAdapter = SessionRVAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        mainViewModel.runsOrderByDate.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) {
                binding.tvNoSessions.visibility = View.VISIBLE
            } else {
                binding.tvNoSessions.visibility = View.GONE
                sessionAdapter.submitList(it)
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvAllSessions.apply {
            adapter = sessionAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onSessionClick(session: Session) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}