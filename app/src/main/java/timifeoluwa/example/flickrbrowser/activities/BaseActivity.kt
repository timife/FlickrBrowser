package timifeoluwa.example.flickrbrowser.activities

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import timifeoluwa.example.flickrbrowser.R

private const val TAG ="BaseActivity"
internal const val FLICKR_QUERY = "FLICKR_QUERY"
internal const val PHOTO_TRANSFER = "PHOTO_TRANSFER"

@SuppressLint("Registered")
open class BaseActivity:AppCompatActivity() {  //This is a class to be extended by more than one activity , so as to avoid writing the function repeatedly
    internal fun activateToolbar(enableHome: Boolean){  //this is to determine if the activity will have a toolbar.the main activity won't need a home button, but the photo details will.
        //so from the details activity , the enable home will get the activity to return to the main activity.
        Log.d(TAG, ".activateToolbar")

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome) //code to include the toolbar.
    }
}