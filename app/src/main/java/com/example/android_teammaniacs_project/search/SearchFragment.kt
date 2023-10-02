import android.accounts.NetworkErrorException
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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.android_teammaniacs_project.constants.SearchButtonType
import com.example.android_teammaniacs_project.retrofit.RetrofitClient.apiService
import com.example.android_teammaniacs_project.search.NoResultsException
import com.example.android_teammaniacs_project.search.SearchViewModel
import com.example.android_teammaniacs_project.search.SearchViewModelFactory
import kotlin.math.max

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
    private lateinit var gridManager: StaggeredGridLayoutManager

    //API 연동을 위해 입력할 값들 정의
    private val key = GoogleKey.KEY
    private val part = "snippet"
    private val maxResults = 20
    private var order = "date"
    private val type = "video"
    private var lastQuery: String? = ""
    private var isRequestInProgress = true

    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0

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
            lastQuery = binding.etSearch.query.toString()

            if (buttonType != SearchButtonType.DATE && lastQuery != "") {
                listAdapter.clearItems()
                try {
                    viewModel.searchVideo(key, part, maxResults, order, lastQuery, type)
                    Log.d("button", order)
                } catch (e: Exception) {
                    handleApiError(e)
                }
            }

            buttonType = SearchButtonType.DATE
        }
        //Rating Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnRating.setOnClickListener {
            order = "rating"
            setButtonSelected(binding.btnRating)
            lastQuery = binding.etSearch.query.toString()

            if (buttonType != SearchButtonType.RATING && lastQuery != "") {
                listAdapter.clearItems()
                try {
                    viewModel.searchVideo(key, part, maxResults, order, lastQuery, type)
                    Log.d("button", order)
                } catch (e: Exception) {
                    handleApiError(e)
                }
            }

            buttonType = SearchButtonType.RATING
        }
        //Title Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnTitle.setOnClickListener {
            order = "title"
            setButtonSelected(binding.btnTitle)
            lastQuery = binding.etSearch.query.toString()

            if (buttonType != SearchButtonType.TITLE && lastQuery != "") {
                listAdapter.clearItems()
                try {
                    viewModel.searchVideo(key, part, maxResults, order, lastQuery, type)
                    Log.d("button", order)
                } catch (e: Exception) {
                    handleApiError(e)
                }
            }

            buttonType = SearchButtonType.TITLE
        }
        //Count Button 클릭/ order 값 변경, query 값과 전에 눌려있던 버튼 값 확인 후 viewmodel에서 function 호출
        binding.btnCount.setOnClickListener {
            order = "viewCount"
            setButtonSelected(binding.btnCount)
            lastQuery = binding.etSearch.query.toString()

            if (buttonType != SearchButtonType.COUNT && lastQuery != "") {
                listAdapter.clearItems()
                try {
                    viewModel.searchVideo(key, part, maxResults, order, lastQuery, type)
                    Log.d("button", order)
                } catch (e: Exception) {
                    handleApiError(e)
                }
            }

            buttonType = SearchButtonType.COUNT
        }


        //SearchView에 값 입력 하고 검색 했을 때 API 호출
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Hyunsik", "message $query")
                if (query.isNullOrBlank()) {
                    showToast("검색어를 입력하세요.")
                    return true
                }
                listAdapter.clearItems()

                when (buttonType) {
                    SearchButtonType.DATE, SearchButtonType.RATING,
                    SearchButtonType.TITLE, SearchButtonType.COUNT -> {
                        try {
                            viewModel.searchVideo(key, part, maxResults, order, query, type)
                            lastQuery = query
                        } catch (e: Exception) {
                            handleApiError(e)
                        }
                    }
                    else -> {

                    }
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 여기에 필요한 코드 추가 (필요한 경우)
                return true
            }
        })


    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleApiError(exception: Exception) {
        val errorMessage: String = when (exception) {
            is NetworkErrorException -> "네트워크 오류가 발생했습니다."
            is NullPointerException -> "Api호출이 제한되었습니다."
            is NoResultsException -> "검색 결과가 없습니다."
            else -> "알 수 없는 오류가 발생했습니다."
        }

        showToast(errorMessage)
    }


    private fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { items ->
            if (items.isEmpty()) handleApiError(NoResultsException())
            else
                listAdapter.addItems(items)
        }
    }

    private fun initView() = with(binding) {
        rvVideo.adapter = listAdapter
        gridManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvVideo.layoutManager = gridManager
        rvVideo.addOnScrollListener(onScrollListener)
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
                    viewModel.searchVideoScrolled(key,part,maxResults,order,lastQuery,type)
                    isRequestInProgress = false
                }
//                isRequestInProgress = true
            }
        }

}