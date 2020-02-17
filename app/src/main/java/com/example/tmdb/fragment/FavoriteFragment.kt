package com.example.tmdb.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import com.example.tmdb.R
import com.example.tmdb.RoomTMDBApplication
import com.example.tmdb.adapter.SearchMovieAdapter
import com.example.tmdb.data.Movie
import com.example.tmdb.service.RetrofitInitializer
import kotlinx.android.synthetic.main.fragment_favorite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FavoriteFragment : Fragment() {

    private val adapter = SearchMovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFavoriteRecycler.adapter = adapter

        loadFavoriteDataBase()

        //Swipe to delete - Item A and B
        //Item A
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                //Remove from adapter list
                adapter.removeItem(viewHolder)

                //Remove from Room dataBase
                Log.i("POSITION ITEM LAYOUT", viewHolder.layoutPosition.toString())
                var moviesDel = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
                Log.i("POSITION ITEM LIST", moviesDel[viewHolder.layoutPosition].toString())
                RoomTMDBApplication.movieDao.deleteMovie(moviesDel[viewHolder.layoutPosition])
            }
        }
        //Item B
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvFavoriteRecycler)

        //------------------------

        btRemoveTudo.setOnClickListener {
            var movies = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
            adapter.setList(movies)
        }
    }

    private fun loadFavoriteDataBase() {

        var movies = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
        adapter.setList(movies)

        btRemover.setOnClickListener {

            var movies1 = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList

            RoomTMDBApplication.movieDao.deleteMovie(movies1[0])

            var movies = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
            adapter.setList(movies)
        }

        btSalvar.setOnClickListener {

            var id = 330457 // Frozen

            RetrofitInitializer().apiService()
                .getMovieById(id, "385801b00919de93e960028b6ca5e4cd", "en-US")
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        Log.e("TMDB", t.stackTrace.toString())
                    }

                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {

                        response.body()?.let {
                            Log.i("MOVIE ->", it.toString())

                            val instantLong = System.currentTimeMillis() / 1000
                            it.instant = instantLong.toString()

                            RoomTMDBApplication.movieDao.insert(it)

                            var movies = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
                            adapter.setList(movies)

                        }
                    }
                })


        }
    }

    override fun onResume() {
        super.onResume()
        var movies = RoomTMDBApplication.movieDao.getAllMovies() as ArrayList
        adapter.setList(movies)
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FavoriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}