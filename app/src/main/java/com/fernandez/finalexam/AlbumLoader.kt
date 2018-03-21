package com.fernandez.finalexam

import android.content.AsyncTaskLoader
import android.content.Context

import java.util.ArrayList

/**
 * Created by Acer on 11/02/2017.
 */

class AlbumLoader(context: Context, private val mUrl: String?, private val requestCode: Int) : AsyncTaskLoader<ArrayList<Album>>(context) {

    override fun onStartLoading() {
        forceLoad()
    }

    override fun loadInBackground(): ArrayList<Album>? {
        return if (mUrl == null) null else QueryUtils.fetchAlbumData(mUrl, requestCode)

    }
}
