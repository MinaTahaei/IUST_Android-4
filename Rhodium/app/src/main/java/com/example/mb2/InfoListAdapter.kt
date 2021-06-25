package com.example.mb2

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InfoListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<InfoListAdapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var infos = emptyList<CellInfo>() // Cached copy of infos

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gsm_rssi_ItemView: TextView = itemView.findViewById(R.id.gsm_rssi)
        val strengthItemView: TextView = itemView.findViewById(R.id.strength)
        val time_ItemView: TextView = itemView.findViewById(R.id.time)
        val altitude_ItemView: TextView = itemView.findViewById(R.id.altitude)
        val longitude_ItemView: TextView = itemView.findViewById(R.id.longitude)
        val type_ItemView: TextView = itemView.findViewById(R.id.type)
        val lac_ItemView: TextView = itemView.findViewById(R.id.lac)
        val tac_ItemView: TextView = itemView.findViewById(R.id.tac)
        //val rac_ItemView : TextView = itemView.findViewById(R.id.rac)
        val latency_ItemView: TextView = itemView.findViewById(R.id.latency)
        val content_latency_ItemView: TextView = itemView.findViewById(R.id.content_latency)
        val plmn_ItemView : TextView = itemView.findViewById(R.id.plmn)
        val arfcn_ItemView: TextView = itemView.findViewById(R.id.arfcn)
        val mnc_ItemView: TextView = itemView.findViewById(R.id.mnc)
        val mcc_ItemView: TextView = itemView.findViewById(R.id.mcc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = infos[position]

        holder.strengthItemView.text = "Strength: ${current.strength}"
        holder.time_ItemView.text = "Time: ${current.time}"
        holder.altitude_ItemView.text = "Altitude: ${current.altitude}"
        holder.longitude_ItemView.text = "Longitude: ${current.longitude}"
        holder.type_ItemView.text = "Type: ${current.type}"
        holder.lac_ItemView.text = "Lac: ${current.lac}"
        holder.tac_ItemView.text = "Tac: ${current.tac}"
        //holder.rac_ItemView.text = "Rac: ${current.rac}"
        holder.mnc_ItemView.text = "Mnc: ${current.mnc}"
        holder.mcc_ItemView.text = "Mcc: ${current.mcc}"
        holder.plmn_ItemView.text = "PLMN: ${current.plmn}"
        holder.latency_ItemView.text = "Latency: ${current.latency}"
        holder.arfcn_ItemView.text = "ARFCN: ${current.arfcn}"
        holder.content_latency_ItemView.text = "Content Latency: ${current.content_latency}"



        try {
            val parent: ViewGroup = holder.gsm_rssi_ItemView.parent as ViewGroup

            if (current.type != "GSM")
            {
                parent.removeView(holder.gsm_rssi_ItemView)
            }
            if (current.type != "LTE")
            {
                parent.removeView(holder.tac_ItemView)
            }
            if (current.type == "LTE")
            {
                parent.removeView(holder.lac_ItemView)
            }
        }
        catch (a: TypeCastException)
        {

        }

    }

    internal fun setWords(words: List<CellInfo>) {
        this.infos = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = infos.size
}