package com.fernandez.finalexam

import android.text.TextUtils
import android.util.Log

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.util.ArrayList

/**
 * Created by Acer on 11/02/2017.
 */

class QueryUtils {
    companion object {

        fun fetchAlbumData(requestUrl: String, requestCode: Int): ArrayList<Album>? {

            val url = createUrl(requestUrl)
            var jsonResponse: String? = null
            var albums: ArrayList<Album>? = ArrayList()
            try {
                jsonResponse = makeHttpRequest(url)

            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (requestCode == 1) {
                albums = extractFeatureFromJson(jsonResponse)
            } else if (requestCode == 2) {
                albums = extractFeatureFromJson2(jsonResponse)
            }
            return albums
        }

        private fun createUrl(stringUrl: String): URL? {
            var url: URL? = null
            try {
                url = URL(stringUrl)
            } catch (e: MalformedURLException) {
            }

            return url
        }

        @Throws(IOException::class)
        private fun makeHttpRequest(url: URL?): String {
            var jsonResponse = ""

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse
            }

            var urlConnection: HttpURLConnection? = null
            var inputStream: InputStream? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.readTimeout = 10000
                urlConnection.connectTimeout = 15000
                urlConnection.requestMethod = "GET"
                urlConnection.connect()

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.responseCode == 200) {
                    inputStream = urlConnection.inputStream
                    jsonResponse = readFromStream(inputStream)
                } else {
                }
            } catch (e: IOException) {
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close()
                }
            }
            return jsonResponse
        }

        @Throws(IOException::class)
        private fun readFromStream(inputStream: InputStream?): String {
            val output = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
                val reader = BufferedReader(inputStreamReader)
                var line: String? = reader.readLine()
                while (line != null) {
                    output.append(line)
                    line = reader.readLine()
                }
            }
            return output.toString()
        }

        private fun extractFeatureFromJson(albumJson: String?): ArrayList<Album>? {
            if (TextUtils.isEmpty(albumJson)) {
                return null
            }

            val albums = ArrayList<Album>()
            Log.d("charles", albumJson)

            try {
                val baseJsonResponse = JSONObject(albumJson)
                val albumArray = baseJsonResponse.getJSONObject("topalbums")
                val secondArray = albumArray.getJSONArray("album")

                for (i in 0 until secondArray.length()) {
                    val currentAlbum = secondArray.getJSONObject(i)

                    val name = currentAlbum.getString("name")
                    val artist = currentAlbum.getJSONObject("artist").getString("name")
                    val url = currentAlbum.getString("url")
                    val image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text")

                    val album = Album(name, artist, url, image)
                    albums.add(album)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return albums
        }

        private fun extractFeatureFromJson2(albumJson: String?): ArrayList<Album>? {
            if (TextUtils.isEmpty(albumJson)) {
                return null
            }

            val albums = ArrayList<Album>()
            Log.d("charles", albumJson)
            try {
                val baseJsonResponse = JSONObject(albumJson)
                val albumArray = baseJsonResponse.getJSONObject("results")
                val secondArray = albumArray.getJSONObject("albummatches")
                val thirdArray = secondArray.getJSONArray("album")

                for (i in 0 until thirdArray.length()) {
                    val currentAlbum = thirdArray.getJSONObject(i)

                    val name = currentAlbum.getString("name")
                    val artist = currentAlbum.getString("artist")
                    val url = currentAlbum.getString("url")
                    val image = currentAlbum.getJSONArray("image").getJSONObject(2).getString("#text")

                    val album = Album(name, artist, url, image)
                    albums.add(album)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            return albums
        }
    }
}
