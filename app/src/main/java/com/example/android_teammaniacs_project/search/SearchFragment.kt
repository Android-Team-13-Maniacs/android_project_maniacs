import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentSearchBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity

class SearchFragment : Fragment() {
    companion object {
        fun newInstance() = SearchFragment()
        val VIDEO_POSITION = "video_position"
        val VIDEO_MODEL = "video_model"
    }

    private val listAdapter by lazy {
        SearchAdapter { position, video ->
            val intent = Intent(context,VideoDetailActivity::class.java)
            intent.apply {
                putExtra(VIDEO_POSITION,position)
                putExtra(VIDEO_MODEL,video)
            }
            startActivity(intent)
        }
    }

    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val demoList = ArrayList<Video>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() = with(binding) {
        rvVideo.adapter = listAdapter
        val gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvVideo.layoutManager = gridManager

        for(i in 1..10) {
            demoList.add(Video(null, "title$i", null))
        }
        listAdapter.addItems(demoList)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}