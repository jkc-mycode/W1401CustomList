package kr.ac.kumoh.s20201414.w1401customlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.s20201414.w1401customlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: SongViewModel
    private val songAdapter = SongAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[SongViewModel::class.java]

        binding.listSong.apply {
            layoutManager = LinearLayoutManager(application)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = songAdapter
        }

        model.list.observe(this){
            songAdapter.notifyItemRangeInserted(0, songAdapter.itemCount)
        }

        model.requestSong()
    }
    inner class SongAdapter : RecyclerView.Adapter<SongAdapter.ViewHolder>(){
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnClickListener {
            //과제때 싹 고쳐야함, 애트리뷰트 갯수만큼 필요
            //val txText = itemView.findViewById(android.R.id.text1) as TextView
            //val txText: TextView = itemView.findViewById(android.R.id.text1)
            val txTitle = itemView.findViewById<TextView>(R.id.test1)
            val txSinger = itemView.findViewById<TextView>(R.id.test2)
            val niImage: NetworkImageView = itemView.findViewById(R.id.image);

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
//                Toast.makeText(application,
//                    model.list.value?.get(adapterPosition)?.title,
//                    Toast.LENGTH_SHORT).show()
                val intent = Intent(application, SongActivity::class.java)
                val song = model.list.value?.get(adapterPosition)
                intent.putExtra(SongActivity.KEY_TITLE, song?.title)
                intent.putExtra(SongActivity.KEY_SINGER, song?.singer)
                intent.putExtra(SongActivity.KEY_IMAGE, model.getImageUrl(adapterPosition))
                startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            //통나무 생성할 때 사용
//            val view = layoutInflater.inflate(
//                android.R.layout.simple_list_item_2, //만든 레이아웃에 맞춰서 바꿔야 함
//                parent,
//                false
//            )
            val view = layoutInflater.inflate(
                R.layout.item_song, //만든 레이아웃에 맞춰서 바꿔야 함
                parent,
                false
            )
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) { //시험 중요
            //통나무의 내용을 세팅하는 곳(새로 나오는 부분의) ViewHolder와 세트이기에 같은 개수만큼 변경
            holder.txTitle.text = model.list.value?.get(position)?.title.toString()
            holder.txSinger.text = model.list.value?.get(position)?.singer.toString()
            holder.niImage.setImageUrl(model.getImageUrl(position), model.imageLoader)
        }

        override fun getItemCount() = model.list.value?.size ?: 0
    }
}