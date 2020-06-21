package timifeoluwa.example.flickrbrowser.activities

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_photo_details.*
import timifeoluwa.example.flickrbrowser.R
import timifeoluwa.example.flickrbrowser.data.Photo


class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
        activateToolbar(true)

//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo  //I'm using the activity intent property to retrieve the intent that started this activity; to retrieve the photo object, i used a getExtra function
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER) as Photo
//         photo_title.text = "Title: + photo.title"

        photo_title.text= resources.getString(R.string.photo_title_text, photo.title)
//        photo_tags.text = "Tags: + photo.tags"
        photo_tags.text= resources.getString(R.string.photo_tags_text, photo.tags)  //This is me removing hardcoded strings from my code with string resources placeholders.
        //the getString function will replace any place holder with the appropriate values by using the title and tags fields of the photo object.
//        photo_author.text = photo.author
//        photo_author.text = resources.getString(R.string.photo_author_text,"my","red","car")  //this can be used in translation to another language which has a different arrangement to the english interpretation.
        photo_author.text = resources.getString(R.string.photo_author_text,photo.author)



        Picasso.with(this).load(photo.link)  //the thumbnail URL was in the image property and i stored the full image URL in the link property.
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(photo_image)


    }

}
