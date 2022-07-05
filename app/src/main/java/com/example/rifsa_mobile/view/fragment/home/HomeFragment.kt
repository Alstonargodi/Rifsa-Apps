package com.example.rifsa_mobile.view.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentHomeBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.HarvestFirebaseEntity
import com.example.rifsa_mobile.view.fragment.harvestresult.adapter.HarvestResultRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }

    private var dataList = ArrayList<HarvestFirebaseEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.mainHomeLayout.fullScroll(ScrollView.FOCUS_UP)

        binding.imageView2.setImageResource(R.drawable.mockprofile)


        diseaseCount()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.apply {
            getUserName().observe(viewLifecycleOwner){ name ->
                binding.tvhomeName.text = name
            }
            getUserId().observe(viewLifecycleOwner){ token ->
                getHarvestRemote(token)
            }
        }

        binding.btnHomeHasil.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToHarvetResultFragment()
            )
        }
    }


    private fun getHarvestRemote(token : String){
        lifecycleScope.launch {

                remoteViewModel.readHarvestResult(token)
                    .addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach { child ->
                            child.children.forEach { main ->
                                val data = main.getValue(HarvestFirebaseEntity::class.java)
                                data?.let { dataList.add(data) }
                                showHarvestList(dataList)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        showStatus(error.message)
                    }
                })

        }
    }

    private fun showHarvestList(data : List<HarvestFirebaseEntity>){
        binding.barhomeHarvest.visibility = View.GONE
        val adapter = HarvestResultRecyclerViewAdapter(data)
        val recyclerView = binding.rvHomeHarvest
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter.onDetailCallBack(object : HarvestResultRecyclerViewAdapter.OnDetailCallback{
            override fun onDetailCallback(data: HarvestFirebaseEntity) {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHarvestInsertDetailFragment(data))
            }
        })
    }

    private fun diseaseCount(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ token->

        }
    }

    private fun showStatus(title : String){
        binding.barhomeHarvest.visibility = View.GONE
        binding.tvhomeHarvestStatus.visibility = View.VISIBLE
        binding.tvhomeHarvestStatus.text = title
        Log.d("HomeFragment", title)
    }
}