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
import com.example.android_teammaniacs_project.databinding.FragmentSearchBinding
import com.example.android_teammaniacs_project.detail.VideoDetailActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.constants.SearchButtonType
import com.example.android_teammaniacs_project.retrofit.RetrofitClient.apiService
import com.example.android_teammaniacs_project.search.SearchViewModel
import com.example.android_teammaniacs_project.search.SearchViewModelFactory

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
        const val VIDEO_POSITION = "video_position"
        const val VIDEO_MODEL = "video_model"
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

    private lateinit var viewModel: SearchViewModel
    private lateinit var gridManager: StaggeredGridLayoutManager

    //API 연동을 위해 입력할 값들 정의
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val maxResults = 20
    private var order = "date"
    private val type = "video"
    private var lastQuery: String? = ""

    //Recycler View 하단 판단을 위한 값
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var isRequestInProgress = true

    //Category 선택 버튼
    private var buttonType = SearchButtonType.DATE
    private var selectedButton: Button? = null

    override fun onAttach(context: Context) {
        viewModel = ViewModelProvider(this, SearchViewModelFactory(apiService))[SearchViewModel::class.java]
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModel()
    }

    //observe Live Data
    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            listAdapter.submitList(items)
            listAdapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            isRequestInProgress = it
        }
    }

    private fun initView() = with(binding) {
        rvVideo.adapter = listAdapter
        gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvVideo.layoutManager = gridManager
        rvVideo.addOnScrollListener(onScrollListener)

        //Default Button Selected 세팅
        setButtonSelected(binding.btnDate)

        fabTop.setOnClickListener {
            rvVideo.smoothScrollToPosition(0) // 최상단으로 스크롤
        }

        //Date Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        btnDate.setOnClickListener {
            onClickedCategoryButton("date", btnDate, SearchButtonType.DATE)
        }
        //Rating Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        btnRating.setOnClickListener {
            onClickedCategoryButton("rating", btnRating, SearchButtonType.RATING)
        }
        //Title Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        btnTitle.setOnClickListener {
            onClickedCategoryButton("title", btnTitle, SearchButtonType.TITLE)
        }
        //Count Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        btnCount.setOnClickListener {
            onClickedCategoryButton("viewCount", btnCount, SearchButtonType.COUNT)
        }

        //SearchView에 값 입력 하고 검색 했을 때 API 호출
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listAdapter.submitList(null)
                viewModel.searchVideo(key, part, maxResults, order, query, type)
                lastQuery = query
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

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

    //Category Button 선택 시 호출되는 함수
    private fun onClickedCategoryButton(
        selectedButtonName: String,
        selectedButton: Button,
        selectedButtonType: SearchButtonType
    ) {
        order = selectedButtonName
        setButtonSelected(selectedButton)
        lastQuery = binding.etSearch.query.toString()

        if (buttonType != selectedButtonType && lastQuery != "") {
            listAdapter.submitList(null)
            viewModel.searchVideo(key, part, maxResults, order, lastQuery, type)
        }

        buttonType = selectedButtonType
    }

    //스크롤 될 때
    private var onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = gridManager.childCount
                totalItemCount = gridManager.itemCount

                val firstVisibleItems = gridManager.findFirstVisibleItemPositions(null)
                if (firstVisibleItems.isNotEmpty()) {
                    pastVisibleItems = firstVisibleItems[0]
                }

                if (isRequestInProgress && visibleItemCount + pastVisibleItems >= totalItemCount) {
                    viewModel.searchVideoScrolled(key, part, maxResults, order, lastQuery, type)
                    isRequestInProgress = false
                }

                val fabUpArrow = binding.fabTop
                if (dy > 0) {
                    fabUpArrow.show() // 아래로 스크롤하면 플로팅 버튼 보이기
                } else {
                    fabUpArrow.hide() // 위로 스크롤하면 플로팅 버튼 숨기기
                }
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}