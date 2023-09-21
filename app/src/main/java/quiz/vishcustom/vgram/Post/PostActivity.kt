package quiz.vishcustom.vgram.Post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import quiz.vishcustom.vgram.HomeActivity
import quiz.vishcustom.vgram.Models.Post
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.databinding.ActivityPostBinding
import quiz.vishcustom.vgram.utils.POST
import quiz.vishcustom.vgram.utils.POST_FOLDER
import quiz.vishcustom.vgram.utils.USER_NODE
import quiz.vishcustom.vgram.utils.uploadImage

class PostActivity : AppCompatActivity() {

    val binding by lazy{
        ActivityPostBinding.inflate(layoutInflater)
    }

    //variable for image url
    var imageUrl: String? = null

    //launching the gallery for post
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER) {
                url ->
                if (url!= null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url

                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //for getting the toolbar
        setSupportActionBar(binding.materialToolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener{
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }


        //allowing to choose image from gallery
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        //cancel button
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }

        //for posting image url
        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    var user =it.toObject<User>()!!
                    val post: Post = Post(postUrl = imageUrl!!,
                        caption=binding.caption.editText?.text.toString(),
                        uid = Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )

                    Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                            .addOnSuccessListener {
                                startActivity(Intent(this@PostActivity,HomeActivity::class.java))
                                finish()
                            }

                    }
                }

        }


    }
}