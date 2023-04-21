@file:Suppress("DEPRECATION")

package com.example.memer

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.memer.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val url: String="https://meme-api.com/gimme"
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMemeData()


        binding.nextbutton.setOnClickListener{
            getMemeData()
        }
        binding.sharebutton.setOnClickListener{
            sharememe()
        }
    }

    private fun getMemeData() {

        val progressDialog=ProgressDialog(this)
        progressDialog.setMessage("Please wait ..|.")
        progressDialog.show()




        val queue = Volley.newRequestQueue(this)



        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.e("Responce", "getMemeData: "+response.toString())

                val responseObject=JSONObject(response)

                binding.memeTitle.text=responseObject.getString("title")
                binding.memeAuthor.text=responseObject.getString("author")
                //binding.imageView.
                Glide.with(this).load( responseObject.get("url")).into(binding.imageView)
                progressDialog.dismiss()
            },
            {
                error->
                progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, error.localizedMessage,Toast.LENGTH_SHORT).show()
            })


        queue.add(stringRequest)
    }

    fun sharememe() {
        val intent= Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,"check this out ..|. $url")
        val chooser=Intent.createChooser(intent, "share this using ....")
        startActivity(chooser)

    }

}