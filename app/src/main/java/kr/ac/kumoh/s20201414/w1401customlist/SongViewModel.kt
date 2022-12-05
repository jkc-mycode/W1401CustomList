package kr.ac.kumoh.s20201414.w1401customlist

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class SongViewModel(application: Application) : AndroidViewModel(application) {
    data class Song(var id: Int, var title: String, var singer: String)

    companion object {
        const val QUEUE_TAG = "SongVolleyRequest"
    }

    private val songs = ArrayList<Song>()
    private val _list = MutableLiveData<ArrayList<Song>>()
    val list: LiveData<ArrayList<Song>>
        get() = _list

    private val queue: RequestQueue

    init {
        _list.value = songs
        queue = Volley.newRequestQueue(getApplication())
    }

    fun requestSong() {
        //requeset 객체를 만드는 곳
        //그 객체를 큐에 담아서 사용
        val request = JsonArrayRequest( //타입으로 받을 때는 대문자
            Request.Method.GET,
            "https://expresssongdb-kqhta.run.goorm.io/song",
            null,
            {
                //Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
                songs.clear()
                parseJson(it)
                _list.value = songs
            },
            {
                Toast.makeText(getApplication(), it.toString(), Toast.LENGTH_LONG).show()
            }
        )
        request.tag = QUEUE_TAG
        queue.add(request)
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }

    private fun parseJson(items: JSONArray){
        for (i in 0 until items.length()){
            val item = items[i] as JSONObject
            val id = item.getInt("id")
            val title = item.getString("title")
            val singer = item.getString("singer")
            songs.add(Song(id, title, singer))
        }
    }
}