package timifeoluwa.example.flickrbrowser.activities

import android.app.SearchManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.annotation.RequiresApi
import timifeoluwa.example.flickrbrowser.R


private const val TAG = "SearchActivity"

@Suppress("DEPRECATION")
class SearchActivity : BaseActivity() {
    private var searchView: SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, ".onCreate: starts")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activateToolbar(true)
        Log.d(TAG, ".onCreate: ends")
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d(TAG, "onCreateOptionsMenu: starts")
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager  //searchManager provides access to the system search services which i got by calling the getSystemService.searchManager retrieves and associates the searchable configuration with the searchView widgets created in the toolbar.
        searchView =
            menu.findItem(R.id.app_bar_search).actionView as SearchView  //got a reference to the searchView widget that is embedded in the search menu_item.
        val searchableInfo =
            searchManager.getSearchableInfo(componentName)   //got the searchManager to retrieve the searchableInfo from searchable.xml
        searchView?.setSearchableInfo(searchableInfo)  //so the searchableInfo is then set into the searchView widget to configure it.


//        Log.d(TAG,".onCreateOptionsMenu: $componentName")
//        Log.d(TAG,".onCreateOptionsMenu: hint is ${searchView?.queryHint}")
//        Log.d(TAG,".onCreateOptionsMenu: $searchableInfo")


        searchView?.isIconified =
            false //tells if the widget should be shown as an icon or should be open and ready for business on click.

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, ".onQueryTextSubmit: called")

                //getting a way to pass the search result back to main activity.sharedPreferences provides a way to store up data, so i can retrieve the data the next time the app starts up.
                val sharedPref =
                    PreferenceManager.getDefaultSharedPreferences(applicationContext)  //we're using appContext here because the storing activity is different from the retrieving activity.
                sharedPref.edit().putString(FLICKR_QUERY, query).apply()  //edit makes the sharedPref writable, and then using the putString function to store the search query string and the FLICKR key stores it to be retrieved in the main activity , apply sends it to the main activity.the data is installed when we call the apply() function.
                 searchView?.clearFocus() //clearFocus is well explained by a device with an external keyboard.
                finish()  //finish ends the activity and returns to whichever activity launched it,here, main activity.
                return true

            }//to get the data , we call it from the mainActivity with the pair KEY

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d(TAG, ".onQueryTextChange: executed")
                return false
            }
        })
        searchView?.setOnCloseListener {
            Log.d(TAG, ".onClose: called")
            finish()
            true
        }
        Log.d(TAG, ".onCreateOptionsMenu: returning")

        return true
    }//N-B: Inflating an XML layout, just takes the XML representation of all the widgets and menu items and so forth, and creates a view for them.Here, i'm using a menuInflater rather
    //than a layoutInflater.When i enter a search query and submit the search, android search framework looks in the manifest folder for an activity that has the
    //action.SEARCH intent-filter  and launches the activity.
}
