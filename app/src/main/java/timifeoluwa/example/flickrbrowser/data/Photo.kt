package timifeoluwa.example.flickrbrowser.data

import android.os.Parcel
import android.os.Parcelable

class Photo(
    var title: String?,
    var author: String?,
    private var authorId: String?,
    var link: String?,
    var tags: String?,
    var image: String?
) :
    Parcelable {
    //another interface that can be used is parcelable when serializable is slow, however, it depends on the structure of the object being serialized,or it being implemented correctly
    //which isn't done.


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun toString(): String {
        return "Photo(title='$title', author='$author', authorId='$authorId', link='$link', tags='$tags', image='$image')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(authorId)
        parcel.writeString(link)
        parcel.writeString(tags)
        parcel.writeString(image)
    }
//    @Throws(IOException::class)
//    private fun writeObject(out: java.io.ObjectOutputStream){
//        Log.d("Photo", "writeObject called")
//        out.writeUTF(title)
//        out.writeUTF(author)
//        out.writeUTF(authorId)
//        out.writeUTF(link)
//        out.writeUTF(tags)
//        out.writeUTF(image)
//    }
//    @Throws(IOException::class,ClassNotFoundException::class)
//    private fun readObject(inStream: java.io.ObjectInputStream){
//        Log.d("Photo", "readObject called")
//        title = inStream.readUTF()
//        author = inStream.readUTF()
//        authorId = inStream.readUTF()
//        link = inStream.readUTF()
//        tags = inStream.readUTF()
//        image = inStream.readUTF()
//    }
//    @Throws (ObjectStreamException::class)
//    private fun readObjectNoData(){
//        Log.d("Photo", "readObjectNoData called")
//    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photo> {
        override fun createFromParcel(parcel: Parcel): Photo {
            return Photo(parcel)
        }

        override fun newArray(size: Int): Array<Photo?> {
            return arrayOfNulls(size)
        }
    }
}