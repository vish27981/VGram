package quiz.vishcustom.vgram.Post

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import quiz.vishcustom.vgram.HomeActivity
import quiz.vishcustom.vgram.Models.Reel
import quiz.vishcustom.vgram.Models.User
import quiz.vishcustom.vgram.databinding.ActivityReelsBinding
import quiz.vishcustom.vgram.utils.REEL
import quiz.vishcustom.vgram.utils.REEL_FOLDER
import quiz.vishcustom.vgram.utils.USER_NODE
import quiz.vishcustom.vgram.utils.uploadVideo

class ReelsActivity : AppCompatActivity() {

    val binding by lazy{

        ActivityReelsBinding.inflate(layoutInflater)
    }

    private lateinit var videoUrl:String
     lateinit var progressDialog: ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER, progressDialog) {
                    url ->
                if (url!= null) {
                    videoUrl = url

                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)


        //accessing the video of gallery
        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        //cancel button
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
            finish()
        }

        //posting the reel
        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {

                var user: User= it.toObject<User>()!!

                val reel: Reel = Reel(videoUrl!!,binding.caption.editText?.text.toString(),user.image!!)

                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).document().set(reel)
                        .addOnSuccessListener {
                            startActivity(Intent(this@ReelsActivity,HomeActivity::class.java))
                            finish()
                        }

                }
            }

        }
    }
}