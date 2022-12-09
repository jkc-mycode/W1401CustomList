package kr.ac.kumoh.s20201414.w1401customlist

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.s20201414.w1401customlist.databinding.ActivityMainBinding
import kr.ac.kumoh.s20201414.w1401customlist.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {
    companion object{
        const val KEY_TITLE = "SongTitle"
        const val KEY_SINGER = "SongSinger"
        const val KEY_IMAGE = "SongImage"
    }
    private lateinit var binding: ActivitySongBinding
    private lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySongBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imageLoader = ImageLoader(Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })

        binding.textTitle.text = intent.getStringExtra(KEY_TITLE)
        binding.textSinger.text = intent.getStringExtra(KEY_SINGER)
        binding.imageSong.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
    }
}