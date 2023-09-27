import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android_teammaniacs_project.constants.GoogleKey
import com.example.android_teammaniacs_project.R
import com.example.android_teammaniacs_project.data.Video
import com.example.android_teammaniacs_project.databinding.FragmentSearchBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.android_teammaniacs_project.retrofit.RetrofitClient.apiService
import com.example.android_teammaniacs_project.search.SearchViewModel
import com.example.android_teammaniacs_project.search.SearchViewModelFactory

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
        val VIDEO_POSITION = "video_position"
        val VIDEO_MODEL = "video_model"
    }

    private val listAdapter by lazy {
        SearchAdapter { position, video ->
            val intent = Intent(context, VideoDetailActivity::class.java)
            intent.apply {
                putExtra(VIDEO_POSITION, position)
                putExtra(VIDEO_MODEL, video)
            }
            startActivity(intent)
            activity?.overridePendingTransition(R.drawable.fade_in, R.drawable.fade_out)
        }
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val demoList = ArrayList<Video>()
    private lateinit var viewModel: SearchViewModel

    val key = GoogleKey.KEY
    val part = "snippet"
    val maxResults = 20
    val order = "date"
    val type = "video"

    override fun onAttach(context: Context) {
        viewModel =
            ViewModelProvider(this, SearchViewModelFactory(apiService))[SearchViewModel::class.java]
        super.onAttach(context)
    }

    private var selectedButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()

        setButtonSelected(binding.btnAll)

        binding.btnAll.setOnClickListener {
            setButtonSelected(binding.btnAll)
        }

        binding.btnMusic.setOnClickListener {
            setButtonSelected(binding.btnMusic)
        }

        binding.btnCinema.setOnClickListener {
            setButtonSelected(binding.btnCinema)
        }

        binding.btnKpop.setOnClickListener {
            setButtonSelected(binding.btnKpop)
        }
        binding.etSearch.setOnSearchClickListener {

        }

        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchView(key, part, maxResults, order, query, type)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })


    }


    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            listAdapter.addItems(items)
        }
    }

    private fun initView() = with(binding) {
        rvVideo.adapter = listAdapter
        val gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvVideo.layoutManager = gridManager
    }

    private fun setButtonSelected(button: Button) {

        selectedButton?.apply {
            backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.white)
            isSelected = false
        }


        button.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.blue)
        button.isSelected = true


        selectedButton = button
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}