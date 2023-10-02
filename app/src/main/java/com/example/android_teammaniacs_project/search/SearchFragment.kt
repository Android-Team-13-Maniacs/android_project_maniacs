import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.constants.SearchButtonType
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

    //API 연동을 위해 입력할 값들 정의
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val maxResults = 20
    private var order = "date"
    private val type = "video"

    //button type
    private var buttonType = SearchButtonType.DATE

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

        //Default Button Selected 세팅
        setButtonSelected(binding.btnDate)

        //Date Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnDate.setOnClickListener {
            order = "date"
            setButtonSelected(binding.btnDate)
            val queryInButton = binding.etSearch.query.toString()

            if(buttonType != SearchButtonType.DATE && queryInButton != "") {
                listAdapter.clearItems()
                viewModel.searchView(key, part, maxResults, order, queryInButton, type)
                Log.d("button", order)
            }

            buttonType = SearchButtonType.DATE
        }
        //Rating Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnRating.setOnClickListener {
            order = "rating"
            setButtonSelected(binding.btnRating)
            val queryInButton = binding.etSearch.query.toString()

            if(buttonType != SearchButtonType.RATING && queryInButton != "") {
                listAdapter.clearItems()
                viewModel.searchView(key, part, maxResults, order, queryInButton, type)
                Log.d("button", order)
            }

            buttonType = SearchButtonType.RATING
        }
        //Title Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnTitle.setOnClickListener {
            order = "title"
            setButtonSelected(binding.btnTitle)
            val queryInButton = binding.etSearch.query.toString()

            if(buttonType != SearchButtonType.TITLE && queryInButton != "") {
                listAdapter.clearItems()
                viewModel.searchView(key, part, maxResults, order, queryInButton, type)
                Log.d("button", order)
            }

            buttonType = SearchButtonType.TITLE
        }
        //Count Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnCount.setOnClickListener {
            order = "viewCount"
            setButtonSelected(binding.btnCount)
            val queryInButton = binding.etSearch.query.toString()

            if(buttonType != SearchButtonType.COUNT && queryInButton != "") {
                listAdapter.clearItems()
                viewModel.searchView(key, part, maxResults, order, queryInButton, type)
                Log.d("button", order)
            }

            buttonType = SearchButtonType.COUNT
        }

        //SearchView에 값 입력 하고 검색 했을 때 API 호출
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.clearItems()
                when(buttonType) {
                    SearchButtonType.DATE -> {
                        viewModel.searchView(key, part, maxResults, order, query, type)
                    }
                    SearchButtonType.RATING -> {
                        viewModel.searchView(key, part, maxResults, order, query, type)
                    }
                    SearchButtonType.TITLE -> {
                        viewModel.searchView(key, part, maxResults, order, query, type)
                    }
                    SearchButtonType.COUNT -> {
                        viewModel.searchView(key, part, maxResults, order, query, type)
                    }
                    else -> {

                    }
                }
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

        //fab button
        val fabUpArrow = binding.fabTop
        rvVideo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    fabUpArrow.show() // 아래로 스크롤하면 플로팅 버튼 보이기
                } else {
                    fabUpArrow.hide() // 위로 스크롤하면 플로팅 버튼 숨기기
                }
            }
        })

        fabUpArrow.setOnClickListener {
            rvVideo.smoothScrollToPosition(0) // 최상단으로 스크롤
        }
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