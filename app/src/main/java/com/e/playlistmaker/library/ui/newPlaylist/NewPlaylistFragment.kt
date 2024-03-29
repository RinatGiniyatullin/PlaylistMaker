package com.e.playlistmaker.library.ui.newPlaylist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.e.playlistmaker.library.domain.Playlist
import com.e.playlistmaker.library.ui.listPlaylists.ListPlaylistsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


open class NewPlaylistFragment : Fragment() {

    open val viewModel by viewModel<NewPlaylistViewModel>()
    private val listPlaylistsViewModel by viewModel<ListPlaylistsViewModel>()

    lateinit var binding: FragmentNewPlaylistBinding

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    var loadUri: Uri? = null

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

        if (binding.title.text!!.isEmpty()) {
            titleEditTextIsEmpty()
        }

        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.title_for_new_playlist_dialog))
            .setMessage(getString(R.string.message_for_new_playlist_dialog))
            .setNeutralButton(getString(R.string.neutral_button_for_new_playlist_dialog)) { dialog, which -> }
            .setPositiveButton(getString(R.string.positive_button_for_new_playlist_dialog)) { dialog, which ->
                back()
            }

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
                    uriForImage = if (loadUri != null) {
                        loadUri.toString()
                    } else {
                        EMPTY_STRING
                    }
                )
            )

            listPlaylistsViewModel.showPlaylist()

            Toast.makeText(
                requireActivity(),
                "Плейлист «${binding.title.text}» создан",
                Toast.LENGTH_SHORT
            ).show()
        }

        viewModel.onUiCreate(this)

        binding.cover.setOnClickListener {
            viewModel.cover()
        }

        viewModel.placeholderLiveData.observe(viewLifecycleOwner) { result ->
            if (!result) {
                hidePlaceholder()
            }
        }

        viewModel.pictureLiveData.observe(viewLifecycleOwner) { uri ->
            loadUri = uri
            Glide.with(binding.cover)
                .load(uri)
                .placeholder(R.drawable.cover_placeholder)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(binding.cover)
        }

        binding.title.doOnTextChanged { text, _, _, _ ->
            titleDoOnTextChanged(text)
        }

        binding.description.doOnTextChanged { text, _, _, _ ->
            binding.descriptionInputLayout.inputTextChangeHandler(text)
        }
    }

    fun titleDoOnTextChanged(text: CharSequence?) {
        binding.titleInputLayout.inputTextChangeHandler(text)
        if (text.isNullOrEmpty()) {
            titleEditTextIsEmpty()
        } else {
            titleEditTextIsNotEmpty()
        }
    }

    private fun TextInputLayout.inputTextChangeHandler(text: CharSequence?) {
        if (text.isNullOrEmpty()) this.setInputStrokeColor(EMPTY_INPUT_STROKE_COLOR_ID)
        else this.setInputStrokeColor(FILLED_IN_INPUT_STROKE_COLOR_ID)
    }

    private fun TextInputLayout.setInputStrokeColor(colorStateListId: Int) {
        this.defaultHintTextColor = resources.getColorStateList(colorStateListId, null)
        this.setBoxStrokeColorStateList(resources.getColorStateList(colorStateListId, null))
    }

    private fun titleEditTextIsEmpty() {
        binding.createButton.isEnabled = false
    }

    private fun titleEditTextIsNotEmpty() {
        binding.createButton.isEnabled = true
    }

    private fun hidePlaceholder() {
        binding.cover.background = null
        binding.image.visibility = View.GONE
    }

    open fun checkBeforeExit() {
        if (binding.title.text!!.isNotEmpty()
            || binding.description.text!!.isNotEmpty()
            || binding.cover.background == null
        ) {
            confirmDialog.show()
        } else {
            back()
        }
    }

    internal fun back() {
        findNavController().navigateUp()
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val EMPTY_INPUT_STROKE_COLOR_ID = R.color.empty_stroke_color
        private const val FILLED_IN_INPUT_STROKE_COLOR_ID = R.color.filled_stroke_color
    }
}