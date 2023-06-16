package com.bangkit.gymguru.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.gymguru.R
import com.bangkit.gymguru.adapter.HistoryAdapter
import com.bangkit.gymguru.data.HistoryItem
import com.bangkit.gymguru.databinding.ActivityHistoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyList: MutableList<HistoryItem>
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyList = mutableListOf()
        historyAdapter = HistoryAdapter(historyList)
        recyclerView = findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = historyAdapter

        // Retrieve data from Firebase Realtime Database
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            val resultsRef = FirebaseDatabase.getInstance().getReference("results").child(currentUserId)
            resultsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    historyList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val historyItem = snapshot.getValue(HistoryItem::class.java)
                        historyItem?.let {
                            val itemName = it.resViewText
                            val itemDate = it.date
                            val itemImageUrl = it.imageUrl

                            val updatedHistoryItem = HistoryItem(itemName, itemDate, itemImageUrl)
                            historyList.add(updatedHistoryItem)
                        }
                    }
                    historyAdapter.setHistory(historyList)
                    historyAdapter.notifyDataSetChanged()
                    Log.d("HistoryActivity", "Data retrieved: $historyList")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("HistoryActivity", "Error retrieving data: ${databaseError.message}") // Add this line
                    // Handle the error
                }
            })
        }
    }

}