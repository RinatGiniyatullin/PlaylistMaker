package com.e.playlistmaker.library.ui.newPlaylist

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.ui.playlist.PlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class NewPlaylistFragment : Fragment() {

    private val viewModel by viewModel<NewPlaylistViewModel>()
    private val playlistViewModel by viewModel<PlaylistViewModel>()

    private lateinit var binding: FragmentNewPlaylistBinding

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    val requester = PermissionRequester.instance()

    private var file: File? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (binding.title.text.isEmpty()) {
            titleEditTextIsEmpty()
        }
        if (binding.description.text.isEmpty()) {
            descriptionEditTextIsEmpty()
        }

        binding.title.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    titleEditTextIsEmpty()
                } else {
                    titleEditTextIsNotEmpty()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    descriptionEditTextIsEmpty()
                } else {
                    descriptionEditTextIsNotEmpty()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.title_for_new_playlist_dialog))
            .setMessage(getString(R.string.message_for_new_playlist_dialog))
            .setNeutralButton(getString(R.string.neutral_button_for_new_playlist_dialog)) { dialog, which -> }
            .setPositiveButton(getString(R.string.positive_button_for_new_playlist_dialog)) { dialog, which ->
                back()
            }

        // добавление слушателя для обработки нажатия на кнопку Back
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkBeforeExit()
                }
            })

        binding.buttonBack.setOnClickListener {
            checkBeforeExit()
        }

        binding.createButton.setOnClickListener {
            back()

            viewModel.createPlaylist(
                Playlist(
                    playlistId = 0,
                    title = binding.title.text.toString(),
                    description = binding.description.text.toString(),
                    uriForImage = if (file != null) {
                        file.toString()
                    } else {
                        EMPTY_STRING
                    }
                )
            )

            playlistViewModel.showPlaylist()

            Toast.makeText(
                requireActivity(),
                "Плейлист «${binding.title.text}» создан",
                Toast.LENGTH_SHORT
            ).show()
        }

        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    hidePlaceholder()
                    saveImageToPrivateStorage(uri)
                    val filePath = File(
                        requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                        "myalbum"
                    )
                    file = File(filePath, "first_cover.jpg")
                    binding.cover.setImageURI(file!!.toUri())
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.cover.setOnClickListener {
            lifecycleScope.launch {
                requester.request(Manifest.permission.CAMERA).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }

                        is PermissionResult.Denied.DeniedPermanently -> {}
                        is PermissionResult.Denied.NeedsRationale -> {}
                        is PermissionResult.Cancelled -> {
                            return@collect
                        }
                    }
                }
            }
        }
    }

    private fun titleEditTextIsEmpty() {
        binding.createButton.isEnabled = false
        binding.titleText.visibility = View.GONE
        binding.title.setBackgroundResource(R.drawable.rectangle_edit_text_gray)
    }

    private fun titleEditTextIsNotEmpty() {
        binding.createButton.isEnabled = true
        binding.titleText.visibility = View.VISIBLE
        binding.title.setBackgroundResource(R.drawable.rectangle_edit_text_blue)
    }

    private fun descriptionEditTextIsEmpty() {
        binding.descriptionText.visibility = View.GONE
        binding.description.setBackgroundResource(R.drawable.rectangle_edit_text_gray)
    }

    private fun descriptionEditTextIsNotEmpty() {
        binding.descriptionText.visibility = View.VISIBLE
        binding.description.setBackgroundResource(R.drawable.rectangle_edit_text_blue)
    }

    private fun hidePlaceholder() {
        binding.cover.background = null
        binding.image.visibility = View.GONE
    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "first_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в выше созданный файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun checkBeforeExit() {
        if (binding.title.text.isNotEmpty()
            || binding.description.text.isNotEmpty()
            || binding.cover.background == null
        ) {
            confirmDialog.show()
        } else {
            back()
        }
    }

    private fun back() {
        findNavController().navigateUp()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}