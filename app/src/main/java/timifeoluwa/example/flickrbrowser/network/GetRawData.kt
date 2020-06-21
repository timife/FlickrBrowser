package timifeoluwa.example.flickrbrowser.network

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, ERROR
} // OK means we've got a valid data, NOT_INITIALIZED means that  we haven't  got a valid URL,IDLE meas it's not processing anything, FAILED means we either failed to download
//anything or the data came back empty, ERROR means there is an error with permission or any error whatsoever.


private const val TAG = "GetRawData"


class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>() { //This is me creating the listener just like before just differently.
    //in this case I gave the getRawData instance a reference to the main activity object, by using 'this' from inside the mainActivity class.Now, in case we are not using
    //this for MainActivity, we rather create an interface for the function onDownloadComplete so as to be able to use it for another activity as far as it implements the OnDownloadComplete interface.
//    private val TAG = "GetRawData"
    private var downloadStatus =
        DownloadStatus.IDLE  //Initialized downloadStatus, i.e not processing anything.
    interface OnDownloadComplete{
        fun onDownloadComplete(data:String, status: DownloadStatus)
    } //Interface should begin with a capital letter, so it specifies a single method called onDownloadComplete.Now, i can change the listener type to OnDownloadComplete.

//    private var listener:MainActivity?=null
//    fun setDownloadCompleteListener(callbackObject: MainActivity){
//        listener= callbackObject
//    }// creating a new listener for onDownloadComplete. listener is a callback function.This is us setting a callback function .we're registering our activity as a listener.

    override fun onPostExecute(result: String) {  //In the Top10 the class that downloaded the data also started the parsing i.e. the onPostExecute under the DownloadData class,even though it doesn't perform the parsing itself, it's still responsible for deciding what to do with the parsing
        //so that can only be used for downloading in XML format to avoid problems.It is tightly coupled to XML data.So, it is worth keeping it uncoupled.
        Log.d(TAG, "onPostExecute called")
        listener.onDownloadComplete(result, downloadStatus)  //this is the callback function calling the onDownloadComplete that the data has been downloaded.this is the onPostExecute calling the onDownloadComplete function so as to uncouple the class from other classes like the parsing application.
    }//we're calling the listener's onDownloadComplete function in the onPostExecute.

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
            downloadStatus =
                DownloadStatus.NOT_INITIALIZED  //the individual elements of a params can be null but the params itself can't be null.If the  first element of params is therefore initialized as null,
            // a download status is returned.i.e. we haven't got a valid URL.
            return "No URL specified"
        }
        try {
            downloadStatus =
                DownloadStatus.OK
            return URL(params[0]).readText()  //this readText is enough to take care of the download data , just as in Top10downloader app when reduced to a single line.
            //Takes care of all the streamReader, Buffering and so on.Now, this may not be a valid URL , but we leave the catch block to detect an invalid URL.
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus =
                        DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus =
                        DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data: ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus =
                        DownloadStatus.ERROR
                    "doInBackground: Security Exception : Needs permission? ${e.message}"
                }
                else -> {
                    downloadStatus =
                        DownloadStatus.ERROR
                    "Unknown error: ${e.message}"
                }
            }
            Log.e(TAG, errorMessage)
            return errorMessage
        }
    }

}