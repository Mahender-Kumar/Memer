package com.example.memer

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memer.databinding.ActivityMainBinding

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
          binding.progressBar.visibility=View.VISIBLE
//        val progressDialog=ProgressDialog(this)
//        progressDialog.setMessage("Please wait ..|.")
//        progressDialog.show()




      //  val queue = Volley.newRequestQueue(this)



        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                Log.e("Responce", "getMemeData: "+response.toString())

                //val responseObject=JSONObject(response)

                binding.memeTitle.text=response.getString("title")
                binding.memeAuthor.text=response.getString("author")
                //binding.imageView.
                Glide.with(this).load( response.get("url"))
                    .listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility=View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility=View.GONE
                            return false
                        }
                    })
                    .into(binding.imageView)
       //         progressDialog.dismiss()
            },
            {
                error->
       //         progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, error.localizedMessage,Toast.LENGTH_SHORT).show()
            })


      // queue.add(stringRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun sharememe() {
        val intent= Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"check this out ..|. $url")
        val chooser=Intent.createChooser(intent, "share this using ....")
        startActivity(chooser)

    }

}