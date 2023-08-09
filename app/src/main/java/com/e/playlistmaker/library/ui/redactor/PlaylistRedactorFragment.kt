package com.e.playlistmaker.library.ui.redactor

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.e.playlistmaker.R
import com.e.playlistmaker.library.ui.newPlaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistRedactorFragment : NewPlaylistFragment() {
    override val viewModel by viewModel<PlaylistRedactorViewModel>()

    companion object {
        const val COVER = "cover"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val ID = "id"

        fun createArgs(cover: String, title: String, description: String, playlistId: Int): Bundle =
            bundleOf(COVER to cover, TITLE to title, DESCRIPTION to description, ID to playlistId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cover = requireArguments().getString(COVER)
        val title = requireArguments().getString(TITLE)
        val description = requireArguments().getString(DESCRIPTION)
        val playlistId = requireArguments().getInt(ID)

        if (cover!!.isNotEmpty()) {

            binding.cover.background = null
            binding.image.visibility = View.GONE
            Glide.with(binding.cover)
                .load(cover.toUri())
                .placeholder(R.drawable.cover_placeholder)
                .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.margin_8)))
                .into(binding.cover)
        }
        binding.title.setText(title)
        binding.description.setText(description)
        binding.createButton.text = getString(R.string.save)
        binding.appBarText.text = getString(R.string.redact)

        binding.createButton.setOnClickListener {
            viewModel.updatePlaylistInfo(

                if (loadUri != null) {
                    loadUri.toString()
                } else {
                    cover
                },
                binding.title.text.toString(),
                binding.description.text.toString(),
                playlistId
            )
        }

        viewModel.back.observe(viewLifecycleOwner) { result ->
            if (result) back()
        }
    }

    override fun checkBeforeExit() {
        back()
    }
}