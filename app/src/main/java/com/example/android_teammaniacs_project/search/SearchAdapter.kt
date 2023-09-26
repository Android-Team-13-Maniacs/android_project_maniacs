import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class SearchAdapter(
    private val onClickItem: (Int, Video) -> Unit,
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val list = ArrayList<Video>()

    fun addItems(items: List<Video>) {
        list.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        return ViewHolder(
            VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(
        private val binding: VideoItemBinding,
        private val onClickItem: (Int, Video) -> Unit,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) = with(binding) {
            tvItem.text = item.title

            //recyclerview item clicklistener
            video.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
        }
    }
}