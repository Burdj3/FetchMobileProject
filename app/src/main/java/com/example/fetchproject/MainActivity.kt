package com.example.fetchproject

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private var trimmedList: ArrayList<ListItem> = ArrayList<ListItem>()
    private lateinit var sortedList: List<ListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://fetch-hiring.s3.amazonaws.com/hiring.json"

        // asyncTask is deprecated, but it's what we still use at my current job for now, so this is how I'd choose to write it "currently"
        // and the problem statement just said make it buildable on the latest release, which it is
        AsyncTaskFetch().execute(url).get()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // pass our list into the adapter
        adapter = RecyclerAdapter(sortedList)
        recyclerView.adapter = adapter
    }

    // just because we're not doing anything with the "id" value, I'll cast it to a String for consistency in my adapter
    class ListItem(val listId: String, val name: String, val id: String) {}

    inner class AsyncTaskFetch : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            var text: String = String()
            val connection = URL(url[0]).openConnection() as HttpURLConnection
            try {
                connection.connect()
                text = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } catch (e: Exception) {
                Log.d("Request: ", e.toString())
            } finally {
                connection.disconnect()
            }

            sortJSON(text)
            return text
        }
    }

    // trim and sort our JSON objects
    private fun sortJSON(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray[i] as JSONObject

            // we are told to only display the values with names, so I'll go ahead and trim the list now instead of sorting unnecessary data
            if (jsonObject.has("name") && jsonObject["name"] != "" && !jsonObject.isNull("name"))
                trimmedList?.add(ListItem(jsonObject["listId"].toString(), jsonObject["name"].toString(), jsonObject["id"].toString()))
        }

        sortedList = trimmedList.sortedWith(compareBy<ListItem> { it.listId }.thenBy { trimInt(it.name) })
    }

    // the "id" value is actually the same as this trimmed Int, so if I was free to do whatever, I'd just sort by that
    private fun trimInt(id: String): Int {
        return Integer.parseInt(id.replace("Item ", ""))
    }
}