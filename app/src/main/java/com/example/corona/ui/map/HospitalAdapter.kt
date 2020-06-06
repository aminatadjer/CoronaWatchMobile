package com.example.corona.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.corona.R

class HospitalAdapter: RecyclerView.Adapter<HospitalAdapter.HospitalHolder>()  {

    companion object{
        private var hospitalList:List<Hospital> =ArrayList()
    }

    class HospitalHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val  text_view_nom: TextView =itemView.findViewById(R.id.hospital_name)
        internal val  text_view_address: TextView =itemView.findViewById(R.id.hospital_address)
        internal val  text_view_num: TextView =itemView.findViewById(R.id.hospital_num)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalAdapter.HospitalHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.hospital_item,parent,false)

        return HospitalHolder(itemView)
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }

    override fun onBindViewHolder(holder: HospitalAdapter.HospitalHolder, position: Int) {
        val currentHospital: Hospital = hospitalList.get(position)
        holder.text_view_nom.setText(currentHospital.nom)
        holder.text_view_address.setText(currentHospital.adresse)
        holder.text_view_num.setText(currentHospital.numero)
    }

    fun setHospital(hospitalListt:List<Hospital>)
    {
        hospitalList =hospitalListt
        notifyDataSetChanged()
    }

}