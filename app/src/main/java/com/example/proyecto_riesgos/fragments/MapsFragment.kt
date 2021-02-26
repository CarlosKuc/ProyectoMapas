package com.example.proyecto_riesgos.fragments

import android.content.Intent
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.proyecto_riesgos.MainService
import com.example.proyecto_riesgos.R
import com.example.proyecto_riesgos.Ventana
import com.example.proyecto_riesgos.data.Mapas
import com.example.proyecto_riesgos.data.MapasViewModel
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(){

    private lateinit var viewModel: MapasViewModel
    private lateinit var mapaquery:List<Mapas>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        val act = activity as MainService
        mapFragment?.getMapAsync(act)
        viewModel = ViewModelProvider(this).get(MapasViewModel::class.java)

        when (act.rint){
            1 -> {
                viewModel.getAll().observe(viewLifecycleOwner, Observer {
                    act.drawMarker(
                        it[0].Latitudsql,
                        it[0].Longitudsql,
                        it[0].Radiosql
                    )
                })
            }
            2 -> {
                viewModel.deleteAll()
                act.firstCustomDialog()
                Toast.makeText(act, "Mapa Nuevo", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(act, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
            }
        }
        initubi.setOnClickListener {
            act.marker.remove()
            act.firstCustomDialog()
        }
        guardarsql.setOnClickListener {
            viewModel.deleteAll()
            insertMaptoDB(act.latitudGlobal,act.longitudGlobal,act.radioglobal)
            val intent = Intent(act, Ventana::class.java)
            startActivity(intent)
        }
    }

    private fun insertMaptoDB(lat:Double,lon:Double,rad:Double){
        val slat = lat
        val slon = lon
        val srad = rad

        if(valCheck(slat,slon,srad)){
            val map = Mapas(0, slat, slon, srad)
            viewModel.addMap(map)
            Toast.makeText(requireContext(), "Valores Insertados", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), "Por favor introduzca coordenadas validas",
                Toast.LENGTH_SHORT).show()
        }

    }
    private fun valCheck(lat:Double,log:Double,rad:Double):Boolean{
        return !(lat == 0.1 && log == 0.1 && rad == 0.1)
    }

    private fun setQuery(mapa:List<Mapas>){
        this.mapaquery = mapa
        
    }
}





