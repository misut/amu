package amu

import android.app.role.RoleManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : AppCompatActivity() {
    private lateinit var requestHomeRoleLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // registerForActivityResult 설정
        requestHomeRoleLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            // 결과 처리: 성공 시 기본 런처로 설정됨
            if (result.resultCode == RESULT_OK) {
                // 기본 홈 앱으로 설정 성공
            } else {
                // 사용자 취소 또는 실패
            }
        }

        // 설치된 런처블 앱 목록 쿼리
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = packageManager.queryIntentActivities(mainIntent, 0)

        setContent {
            MaterialTheme {  // Material3 테마 사용
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background  // Material3 색상 스킴
                ) {
                    AppGrid(apps = apps)
                }
            }
        }

        // 기본 홈 역할 요청 (onCreate나 버튼 클릭 시 호출)
        requestHomeRoleIfNeeded()
    }

    private fun requestHomeRoleIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)
            if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME) &&
                !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
            ) {
                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                requestHomeRoleLauncher.launch(intent)
            }
        }
    }
}
