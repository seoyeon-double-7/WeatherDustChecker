package com.example.weatherdustchecker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import wikibook.learnandroid.weatherdustchecker.APICall

@JsonDeserialize(using = MyDust::class)

data class OpenDustAPIJSONResponse(val pm10: Double, val pm25: Double)

class MyDust : StdDeserializer<OpenDustAPIJSONResponse>(
    OpenDustAPIJSONResponse::class.java
){
    override fun deserialize(
        p: JsonParser?,
        ctxt: DeserializationContext?
    ): OpenDustAPIJSONResponse {

        val node = p?.codec?.readTree<JsonNode>(p)

        val data = node?.get("data")
        val iaqi = data?.get("iaqi")
        val pm10 = iaqi?.get("pm10")?.get("v")?.asDouble()
        val pm25 = iaqi?.get("pm25")?.get("v")?.asDouble()

        return OpenDustAPIJSONResponse(
            pm10!!,
            pm25!!
        )
    }

}



class DustTestFragment : Fragment(){
    lateinit var dustImage: ImageView
    lateinit var small_dust : TextView
    lateinit var dust : TextView

    var APP_ID = "64ca5c402f706dae715fc3b651ff58a51ecea8d6"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.dust_text_fragment,
            container, false)

        dustImage = view.findViewById<ImageView>(R.id.dust_icon)
        small_dust = view.findViewById<TextView>(R.id.small_dust_num)
        dust = view.findViewById<TextView>(R.id.dust_num)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lat = arguments?.getDouble("lat")
        val lon = arguments?.getDouble("lon")

        val url = "https://api.openweathermap.org/data/2.5/weather?units=metric&appid=${APP_ID}&lat=${lat}&lon=${lon}"

        APICall(object : APICall.APICallback{
            override fun onComplete(result: String) {
                Log.d("mytag", result)

                var mapper = jacksonObjectMapper()
                var data = mapper?.readValue<OpenDustAPIJSONResponse>(result)
                small_dust.text = data.pm25.toString()
                dust.text = data.pm10.toString()

            }

        })


    }

    companion object{
        fun newInstance(lat: Double, lon: Double)
            : DustTestFragment{
                val fragment = DustTestFragment()
                val args = Bundle()

                args.putDouble("lat", lat)
                args.putDouble("lon", lon)
                fragment.arguments = args

                return fragment
            }
    }


}