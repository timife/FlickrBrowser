package timifeoluwa.example.flickrbrowser.data

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import timifeoluwa.example.flickrbrowser.R

class FlickrImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}//This is just a way of storing references to the widgets in the view that'll be displayed by the recyclerView
private const val TAG = "FlickrRecyclerViewAdapt"

class FlickrRecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<FlickrImageViewHolder>() {

    override fun getItemCount(): Int {
//        Log.d(TAG, "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 1
    }//using if as an expression, that is check.

    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()//this function tells the recyclerView(registered observer) that the data has been changed so that it can refresh the display
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }//this will be useful when we tap one photo in the list of photos.we're going to display a placeholder image if photo is empty.

    override fun onBindViewHolder(
        holder: FlickrImageViewHolder,
        position: Int
    ) {//this method is called by the recyclerView when it wants new data to be stored in a view holder, an existing view.
    if(photoList.isEmpty()){
        holder.thumbnail.setImageResource(R.drawable.placeholder)
        holder.title.setText(R.string.empty_photo)
    }else{
        val photoItem =
            photoList[position] //retrieve the correct photo object from the list.The recyclerView tells
        //the position of the data we need in the position parameter.
        Log.d(TAG, ".onBindViewHolder: ${photoItem.title} --> $position")
        Picasso.with(holder.thumbnail.context) //this is another way to get get view from context, i.e. directly from the context of the thumbnail, rather than declare it in the class constructor above.
            .load(photoItem.image) //use picasso.with function to get a picasso object.So,this is getting a context for picasso to use from the context of the thumbnail imageView.
            //the load function loads an image from our URL and we store the thumbnail URL in the image field of the photo class.
            .error(R.drawable.placeholder)  //sets the placeholder image to be used if there's an error
            .placeholder(R.drawable.placeholder)  //set the placeholder when the images are downloading.
            .into(holder.thumbnail)  //picasso will download the image from the URL on a background thread, and puts it into the imageView once its downloaded and it updates the imageView with the downloaded image.2
        //it doesn't wait for the download to finish, it has the placeholder there first before it updates it.
        holder.title.text = photoItem.title
    }//a common mistake would be to declare the viewHolder class as an inner class of recyclerAdapter, but this would lead to memory leaks.In java we would make the class static.Here, we would place it in the same file as the adapter, so that the
//adapter would have access to the viewHolder class.
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickrImageViewHolder {
        //called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder new view requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickrImageViewHolder(view)
    }//this function was to inflate a view from the browse.XML layout that I created, and then return that view.


}

//N-B: The recyclerView adapter provides the view and the data to be displayed, the layout is handled by the layoutManager and the views themselves live in a view holder.