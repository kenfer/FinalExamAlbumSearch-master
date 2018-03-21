package com.fernandez.finalexam

import android.app.Activity
import android.app.LoaderManager
import android.content.Context
import android.content.Loader
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

import java.util.ArrayList

class SongsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<ArrayList<Album>> {
    private var searchBy = 1
    private var mEmptyStateTextView: TextView? = null
    private var label: TextView? = null
    private var searchTxt: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var loadingIndicator: ProgressBar? = null
    var search = ""
    private var albumsAdapter: AlbumsAdapter? = null
    private var loaderManager: LoaderManager? = null


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)
        title = "Album Finder"

        val bar = supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#E71919")))

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.statusBarColor)

        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        mEmptyStateTextView = findViewById(R.id.empty_view) as TextView
        label = findViewById(R.id.label) as TextView
        searchTxt = findViewById(R.id.searchTxt) as TextView
        loadingIndicator = findViewById(R.id.loading_indicator) as ProgressBar

        mEmptyStateTextView!!.setText(R.string.no_album_present)

        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo


        if (networkInfo != null && networkInfo.isConnected) {


        } else {
            loadingIndicator!!.visibility = View.GONE
            mEmptyStateTextView!!.setText(R.string.no_internet_connection)
        }

        searchTxt!!.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                search = searchTxt!!.text.toString()
                performSearch()
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<ArrayList<Album>> {
        val baseUri = Uri.parse(REQUEST_URL)
        val uriBuilder = baseUri.buildUpon()

        if (id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums")
            uriBuilder.appendQueryParameter("artist", search) // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9")
            uriBuilder.appendQueryParameter("limit", "50")
            uriBuilder.appendQueryParameter("format", "json")
        } else if (id == ALBUM_LOADER_ID2) {
            uriBuilder.appendQueryParameter("method", "album.search")
            uriBuilder.appendQueryParameter("album", search) // edit later
            uriBuilder.appendQueryParameter("api_key", "490b41d76995ab4e15ca4d9d04e015a9")
            uriBuilder.appendQueryParameter("limit", "50")
            uriBuilder.appendQueryParameter("format", "json")
        }
        Log.d("charles", uriBuilder.toString())

        return AlbumLoader(this, uriBuilder.toString(), id)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Album>>, data: ArrayList<Album>?) {
        recyclerView!!.visibility = View.VISIBLE
        albumsAdapter = AlbumsAdapter(this, data)
        if (data != null && !data.isEmpty()) {
            mEmptyStateTextView!!.visibility = View.GONE
            loadingIndicator!!.visibility = View.GONE

            val layoutManager = LinearLayoutManager(this)
            recyclerView!!.layoutManager = layoutManager
            recyclerView!!.adapter = albumsAdapter
        } else {
            recyclerView!!.visibility = View.GONE
            loadingIndicator!!.visibility = View.GONE
            mEmptyStateTextView!!.visibility = View.VISIBLE
        }
        getLoaderManager().destroyLoader(searchBy)
        //getLoaderManager().destroyLoader(ALBUM_LOADER_ID2);
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Album>>) {}

    fun performSearch() {
        mEmptyStateTextView!!.visibility = View.GONE
        loadingIndicator!!.visibility = View.VISIBLE
        loaderManager = getLoaderManager()
        loaderManager!!.initLoader(searchBy, null, this)
        //loaderManager.initLoader(ALBUM_LOADER_ID2 ,null, this);
        hideSoftKeyboard(this@SongsActivity)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.clear) {
            searchTxt!!.text = ""
            recyclerView!!.visibility = View.GONE
            mEmptyStateTextView!!.visibility = View.VISIBLE
        }
        if (id == R.id.artistName) {
            label!!.text = "Search album by artist"
            searchBy = 1
        }
        if (id == R.id.albumName) {
            label!!.text = "Search album by name"
            searchBy = 2
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        //            "method=artist.gettopalbums" +
        //            "&artist=cher" +
        //            "&api_key=490b41d76995ab4e15ca4d9d04e015a9" +
        //            "&format=json";

        private val REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?"

        private val ALBUM_LOADER_ID1 = 1
        private val ALBUM_LOADER_ID2 = 2

        fun hideSoftKeyboard(activity: Activity) {
            val inputMethodManager = activity.getSystemService(
                    Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                    activity.currentFocus!!.windowToken, 0)
        }
    }
}
