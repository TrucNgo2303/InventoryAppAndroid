package com.example.inventoryapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.example.inventoryapp.ui.api.AssetDTO

class AssetAdapter(private val assetList: List<AssetDTO>) :
    RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    class AssetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sttTV: AppCompatTextView = itemView.findViewById(R.id.STT)
        val assetName: AppCompatTextView = itemView.findViewById(R.id.item_assetname)
        val assetType: AppCompatTextView = itemView.findViewById(R.id.item_assettype)
        val dateChecked: AppCompatTextView = itemView.findViewById(R.id.item_date)
        val status: AppCompatTextView = itemView.findViewById(R.id.item_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assetList[position]
        holder.sttTV.text = (position + 1).toString()
        holder.assetName.text = asset.assetName
        holder.assetType.text = asset.assetType.toString()
        val inventoryDateFormatted = asset.inventoryDate.split("T")[0]
        holder.dateChecked.text = inventoryDateFormatted
        holder.status.text = asset.status.toString()
    }

    override fun getItemCount(): Int = assetList.size
}