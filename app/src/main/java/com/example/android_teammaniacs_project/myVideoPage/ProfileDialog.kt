package com.example.android_teammaniacs_project.myVideoPage

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.android_teammaniacs_project.databinding.DialogEditBinding

class ProfileDialog(
    private val editName: String?,
    private val editImageUri: Uri?,
    private val okCallback: (String, Uri?) -> Unit,
) : DialogFragment() {


    private lateinit var binding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST_CODE = 123
        const val PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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
            checkGalleryPermissionAndNavigateGallery()
        }

        editName?.let { etContent.setText(it) }
        editImageUri?.let {
            selectedImageUri = it
            ivProfile.setImageURI(it)
        }
    }

    private fun checkGalleryPermissionAndNavigateGallery() {
        val context = context ?: return
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                navigateGallery()
            }
            shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionContextPopup()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(requireContext())
            .setTitle("권한 필요")
            .setMessage("프로필 사진을 변경하려면 저장소 읽기 권한이 필요합니다.")
            .setPositiveButton("동의") { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            }
            .setNegativeButton("취소") { _, _ -> }
            .create()
            .show()
    }

    private fun navigateGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            selectedImageUri = imageUri
            binding.ivProfile.setImageURI(imageUri)
        }
    }
}
