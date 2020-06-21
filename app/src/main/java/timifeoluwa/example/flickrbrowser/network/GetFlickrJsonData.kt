package timifeoluwa.example.flickrbrowser.network

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import timifeoluwa.example.flickrbrowser.data.Photo

private const val TAG = "GetFlickrJsonData"

class GetFlickrJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<Photo>>() {

    interface OnDataAvailable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)  //we want to let whatever uses this class to know if there's been an error.we could have used the enum class just like we did with the
        //status for the getRawData class but chose to do things a little differently by using an interface.
    }

    override fun onPostExecute(result: ArrayList<Photo>) {
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG, ".onPostExecute ends")
    }

    override fun doInBackground(vararg params: String): ArrayList<Photo> {
        Log.d(TAG, "doInBackground starts")
        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemArray = jsonData.getJSONArray("items")

            for (i in 0 until itemArray.length()) {
                val jsonPhoto = itemArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")
                val jsonMedia = jsonPhoto.getJSONObject("media")
                val image = jsonMedia.getString("m")
                val link = image.replaceFirst("_m.jpg", "_b.jpg")

                val photoObject = Photo(
                    title,
                    author,
                    authorId,
                    link,
                    tags,
                    image
                )
                photoList.add(photoObject)     //This links the photo arrayList to the photoObjects i.e the data downloaded is parsed.
                Log.d(TAG, ".doInBackground $photoObject")
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.e(TAG, ".doInBackground: Error processing Json data. ${e.message}")
            cancel(true)    //allows the main thread to cancel a long running task if necessary.So if we cancel the task when there's an
            //an exception, the async task won't call the onPostExecute rather than showing a blank result before.
            listener.onError(e)
        }//this code is running on a separate thread, so the calling code can't trap the error in a try or catch block, so we have to use this mechanism to let the caller know about any error that may happen.
       //N-B if trying to catch an error from another activity , we rather use this mechanism than the try or catch block.
        Log.d(TAG, "doInBackground ends")
        return photoList
    }
}