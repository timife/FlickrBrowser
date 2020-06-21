package timifeoluwa.example.flickrbrowser.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.content_main.*
import timifeoluwa.example.flickrbrowser.*
import timifeoluwa.example.flickrbrowser.data.FlickrRecyclerViewAdapter
import timifeoluwa.example.flickrbrowser.data.Photo
import timifeoluwa.example.flickrbrowser.network.DownloadStatus
import timifeoluwa.example.flickrbrowser.network.GetFlickrJsonData
import timifeoluwa.example.flickrbrowser.network.GetRawData


private const val TAG = "MainActivity"


@Suppress("DEPRECATION")
class MainActivity : BaseActivity
    (),
    GetRawData.OnDownloadComplete,
    GetFlickrJsonData.OnDataAvailable,
    RecyclerItemClickListener.OnRecyclerClickListener {   //This is the interface being implemented, so it can also be implemented freely in another activity that
    //is not MainActivity.
//    private val TAG= "MainActivity"
    private val flickrRecyclerViewAdapter =
        FlickrRecyclerViewAdapter(
            ArrayList()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateToolbar(false)
        recycler_view.layoutManager =
            LinearLayoutManager(this) //the layoutManager takes care of handling the layout not the recyclerView.
        recycler_view.addOnItemTouchListener(
            RecyclerItemClickListener(
                this, recycler_view, this
            )//this is creating a new instance of recyclerItemClickListener class and add it as a touch listener to the recyclerView.
        )//the first 'this' is passing it as a context because this is an activity,
        //and the second  a listener because we've implemented the required interface.
        recycler_view.adapter =
            flickrRecyclerViewAdapter  //we associate the new instance of our recyclerView adapter with the recyclerView
        //by assigning my adapter to the recyclerView.adapter property.

        val url = createUri(
            "https://api.flickr.com/services/feeds/photos_public.gne",
            "android,oreo",
            "en-us",
            true
        )
        val getRawData =
            GetRawData(this)  //this is us passing the getRawData to the onDownloadComplete in the MainActivity after download is complete.

//        getRawData.setDownloadCompleteListener(this)//the class that wants to know about the download complete passes its object to the getRawData just as in the case for buttons i.e the onDownload completeListeners
        //we're here notifying the calling class that data has been downloaded.getRawData here is like the button attached to the objects onClickListeners in other projects.
        //synonymously here we want to respond when download is complete.so when the async task completes, the onComplete listener is executed.it refers to the current instance of our class.
        //.. here we're telling getRawData to call the onDownloadComplete function in this instance of mainActivity.

        getRawData.execute(url)

//        fab.setOnClickListener { view ->
//            SnackBar.make(view, "Replace with your own action", SnackBar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        Log.d(TAG, "onCreate ends")


    }

    //The condition in which the app will be used will determine which of the clicks will be used.for example , the long click is used in a sat nav for cyclists because of their hand gloves.
    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG, ".onItemClick: starts")
        Toast.makeText(this, "Normal tap at position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG, ".onItemLongClick: starts")
        val photo =
            flickrRecyclerViewAdapter.getPhoto(position)//getting the photo details by using the getPhoto function.
        if (photo != null) {
            val intent = Intent(
                this,
                PhotoDetailsActivity::class.java
            )  //::class.java is the class literal that we can use to pass the class as a parameter
            intent.putExtra(
                PHOTO_TRANSFER,
                photo
            )
            //putExtra function is used to add data to the intent.this is like the bundle used in saveInstanceState and restore where we needed a key and a data to get the data from another activity.
            //putExtra can be used to add more complex objects to the intent  but the object must be serializable(it can be stored and retrieved)
            //To serialize an object means to convert its state to a byte stream so that the byte stream can be reverted back into a copy of the object.
            startActivity(intent)



        }
    }

    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchAll: Boolean
    ): String {
        Log.d(TAG, ".createUri starts")

        return Uri.parse(baseURL)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
//           var uri = Uri.parse(baseUri)
//           var builder = uri.buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build().toString()
//          uri = builder.build()

    }//this is to create the uri by editing according to searches , changing the parameter or adding parameters to the baseUri. the .appendQueryParameter takes
    //care of the ampersand(&) and the question mark symbol(?) separating parameters.The appendQueryParameter appends two arguments, the first a string, that's the name of the
    //parameter that we want to add to the url, the second one is the value that we want to specify for the parameter.

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu called")

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected called")



        return when (item.itemId) {
            R.id.action_search -> {
                val intent= Intent(this,
                    SearchActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    companion object{ //if we used a normal variable , each instance of MainActivity will create a new TAG variable which is not a good use of memory.
//        //that is why we should class variables instead of instance variables.Now, in java it is called companion object.
//        const private val TAG= "MainActivity"
//    }
    override fun onDownloadComplete(
        data: String,
        status: DownloadStatus
    ) {      //This function is to uncouple the onPostExecute from all other classes to make it useful for other projects and also to make it useful for other types of data
        //apart from XML data. That is, it is called by a different class from the one responsible for downloading the data. In the Top10 the class that downloaded the data also started the parsing i.e. the onPostExecute under the DownloadData class,even though it doesn't perform the parsing itself, it's still responsible for deciding what to do with the parsing
        // so that can only be used for downloading in XML format to avoid problems.It is tightly coupled to XML data.So, it is worth keeping it uncoupled.So what we're doing is notify this class that data has been downloaded and somehow make the data available to it. When the getRawData is done it calls this class back using callBack method.
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called")

            val getFlickrJsonData =
                GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            //download failed
            Log.d(TAG, "onDownloadComplete failed")
        }


    }// we need to notify the getRawData of this function so as to call it after the getRawData has downloaded it's data  using a callback method

    //this is like when a button gets clicked, the class that wants to know about the click creates an object that has a function that responds to the click,the calling class
    //then passes the object to the button.These are the onClickListeners attached to buttons in my other projects.
    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, ".onDataAvailable called")
        flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG, ".onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
    }

    override fun onResume() {
        Log.d(TAG,".onResume starts")

        super.onResume()

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY,"")  //passing an empty allows to check to make sure a search term is gotten from the user. the getString function will attempt to retrieve the data stored with the key flickr query.but if it
        //can't find the data with that key , it's going to return the second value passed, which here is an empty string.
        if (queryResult!!.isNotEmpty()){
            val url = createUri("https://api.flickr.com/services/feeds/photos_public.gne", queryResult, "en-us", true
            )
            val getRawData =
                GetRawData(this)
            getRawData.execute(url)

        }
        Log.d(TAG,".onResume ends")


    }
    //These are executed after the onPostExecute in the data parser getFlickrJsonData.
}
