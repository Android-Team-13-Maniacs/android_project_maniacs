import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.VideoItemBinding

class SearchAdapter(
    private val onClickItem: (Int, Video) -> Unit,
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val list = ArrayList<Video>()

    fun addItems(items: List<Video>) {
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        list.clear()
        notifyDataSetChanged()
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
            Glide.with(itemView).load(item.image)
                .placeholder(R.drawable.img_loding) // 이미지 로딩 중 사진
                .error(R.drawable.img_no) // 이미지를 불러오지 못했을 때 사진
                .into(ivItem)
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