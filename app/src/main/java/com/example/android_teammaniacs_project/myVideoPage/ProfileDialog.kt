import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.android_teammaniacs_project.databinding.DialogEditBinding

class ProfileDialog(
    context: Context,
    private val editlName: String?,
    private val editImageUri: Uri?,
    private val okCallback: (String, Uri?) -> Unit,
) : Dialog(context) {

    private lateinit var binding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnSave.setOnClickListener {
            if (etContent.text.isNullOrBlank()) {
                Toast.makeText(context, "닉네임을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                okCallback(etContent.text.toString(), selectedImageUri)
                dismiss()
            }
        }
        btnCancle.setOnClickListener {
            dismiss()
        }
            ivProfile.setOnClickListener {
            Toast.makeText(context, "클릭했습니다!", Toast.LENGTH_SHORT).show()
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (context as? Activity)?.startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }
        editlName?.let { etContent.setText(it) }
        editImageUri?.let {
            selectedImageUri = it
            ivProfile.setImageURI(it)
        }
    }
}
