package com.balex.activityresultapi

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var getUsernameButton: Button
    private lateinit var usernameTextView: TextView
    private lateinit var getImageButton: Button
    private lateinit var imageFromGalleryImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val contract = object : ActivityResultContract<Intent, String?>() {
            override fun createIntent(context: Context, input: Intent): Intent {
                return input
            }

            override fun parseResult(resultCode: Int, intent: Intent?): String? {
                if (resultCode == RESULT_OK) {
                    return intent?.getStringExtra(UsernameActivity.EXTRA_USERNAME) ?: ""
                }
                return null
            }
        }

        val launcher = registerForActivityResult(contract) {
            if (!it.isNullOrBlank()) {
                usernameTextView.text = it
            }
        }

        val contractImage = object : ActivityResultContract<String, Uri?>() {
            override fun createIntent(context: Context, input: String): Intent {
                return Intent(Intent.ACTION_PICK).apply {
                    type = input
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return intent?.data
            }
        }

        val launcherImage = registerForActivityResult(contractImage) {
            imageFromGalleryImageView.setImageURI(it)

        }



        getUsernameButton.setOnClickListener {
            launcher.launch(UsernameActivity.newIntent(this))
        }
        getImageButton.setOnClickListener {
            launcherImage.launch("image/*")

        }
    }


    private fun initViews() {
        getUsernameButton = findViewById(R.id.get_username_button)
        usernameTextView = findViewById(R.id.username_textview)
        getImageButton = findViewById(R.id.get_image_button)
        imageFromGalleryImageView = findViewById(R.id.image_from_gallery_imageview)
    }

}