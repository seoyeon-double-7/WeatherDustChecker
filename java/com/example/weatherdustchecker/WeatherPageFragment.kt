package com.example.weatherdustchecker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeatherPageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.weather_page_fragment,
            container, false)

//      ToDO: arguments 값 참조해서 두개 값 가져오고, 해당하는 뷰에 출력해주기

        val status = view.findViewById<TextView>(R.id.weather_status_text)
        val temperature = view.findViewById<TextView>(R.id.weather_temp_text)

//      Todo : ImagaViw 가져와서 sun 이미지 출력하기
        val weatherImage = view.findViewById<ImageView>(R.id.weather_icon)

        weatherImage.setImageResource(arguments?.getInt("res_id")!!)

        status.text = arguments?.getString("status")
        temperature.text = arguments?.getDouble("temperature").toString()


        return view
    }
    companion object{
        fun newInstance(status: String, temperature: Double) : WeatherPageFragment{
            val fragment = WeatherPageFragment()

            val args = Bundle()
            args.putString("status", status)
            args.putDouble("temperature", temperature)
            args.putInt("res_id", R.drawable.sun)
            fragment.arguments = args

            return fragment
        }
    }
}