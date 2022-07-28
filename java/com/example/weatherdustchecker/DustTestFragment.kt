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
import java.net.URL

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

        val url = "https://api.waqi.info/feed/geo:${lat};${lon}/?token=${APP_ID}"
        APICall(object : APICall.APICallback{
            override fun onComplete(result: String) {
                fun asd(dust_num : Double): String {
                    if(dust_num >=0 && dust_num <=100){
                        return "좋음"
                    }else if(dust_num >=101 && dust_num <=200){
                        return "보통"
                    }
                    else {
                        return "나쁨"
                    }

                }
                Log.d("mytag", result)

                var mapper = jacksonObjectMapper()
                var data = mapper?.readValue<OpenDustAPIJSONResponse>(result)
                small_dust.text = data.pm25.toString()
                dust.text = data.pm10.toString()

                val small_num_statement = view.findViewById<TextView>(R.id.small_dust)
                val dust_statement = view.findViewById<TextView>(R.id.dust)
                small_num_statement.text = "${asd(data.pm25)}(초미세먼지)"
                dust_statement.text = "${asd(data.pm10)}(초미세먼지)"

                if(asd(data.pm25) == "좋음"){
                    dustImage.setImageResource(R.drawable.good)
                } else if(asd(data.pm25) == "보통"){
                    dustImage.setImageResource(R.drawable.normal)
                } else if(asd(data.pm25) == "나쁨"){
                    dustImage.setImageResource(R.drawable.bad)
                }

            }

        }).execute(URL(url))


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