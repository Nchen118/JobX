package com.example.jobx

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.jobx.admin.AdminActivity
import com.example.jobx.chat.ChatList
import com.example.jobx.company.ApproveJobRequest
import com.example.jobx.company.CompanyJobList
import com.example.jobx.company.InsertJob
import com.example.jobx.jobseeker.CompanyList
import com.example.jobx.jobseeker.JobList
import com.example.jobx.jobseeker.update_profile
import com.example.jobx.library.Adapter
import com.example.jobx.library.Common
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main_page.*
import kotlinx.android.synthetic.main.login_fragment.view.*

class MainPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        setSupportActionBar(toolbar)

        val tabs = findViewById<TabLayout>(R.id.tab)
        val viewPage = findViewById<ViewPager>(R.id.pager)
        val fragmentAdapter = Adapter(supportFragmentManager)

        fabChat.setOnClickListener {
            val intent = Intent(this,ChatList::class.java)
            startActivity(intent)
        }
        logoutIcon.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, LoadingPage::class.java),
                ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )
            this.finish()
        }
        editProfile.setOnClickListener {
            startActivity(Intent(this,update_profile::class.java))
        }

        if (Common.user?.position == "jobseeker") {
            fragmentAdapter.addFragment(InfoWall(), "Page 1")
            fragmentAdapter.addFragment(JobList(), "Job")
            fragmentAdapter.addFragment(CompanyList(), "Company")
            fragmentAdapter.addFragment(LogoutPage(), "Logout")
            supportActionBar?.title = "Job Seeker"
        } else if (Common.user?.position == "company") {
            fragmentAdapter.addFragment(InfoWall(), "Page 1")
            fragmentAdapter.addFragment(InsertJob(), "Insert")
            fragmentAdapter.addFragment(CompanyJobList(), "Job")
            fragmentAdapter.addFragment(ApproveJobRequest(), "Request List")
            fragmentAdapter.addFragment(LogoutPage(), "Logout")
            supportActionBar?.title = "Company"
        } else if (Common.user?.position == "admin") {
            startActivity(
                Intent(this, AdminActivity::class.java),
                ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                ).toBundle()
            )

            finish()
        } else {
            supportActionBar?.title = "JobX"
        }

        viewPage.adapter = fragmentAdapter
        tabs.setupWithViewPager(viewPage)

        name.text = Common.user.name

    }

}