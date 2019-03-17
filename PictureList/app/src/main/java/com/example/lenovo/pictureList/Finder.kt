import android.content.Context
import android.widget.ImageView
import com.squareup.picasso.Picasso
import java.io.File

object Finder {
    private fun getFilenameFromURL(url: String?): String {
        return "file" + url?.hashCode().toString()
    }

    fun setImageIntoView(context: Context?, url: String?, view: ImageView): Boolean {
        val existingFiles = context?.fileList()
        val filename = getFilenameFromURL(url)
        return if (existingFiles != null && existingFiles.contains(filename)) {
            Picasso.get().load(File(context.filesDir, filename)).into(view)
            true
        } else {
            false
        }
    }
}