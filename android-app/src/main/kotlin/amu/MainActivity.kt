package amu

import android.Manifest
import android.app.WallpaperManager
import android.app.role.RoleManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var requestHomeRoleLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestManageAllFilesLauncher: ActivityResultLauncher<String>

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

        val manageAllFilesAccessPermissionIntent =
            Intent(
                Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${BuildConfig.APPLICATION_ID}")
            )

        requestManageAllFilesLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

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
                    if (Environment.isExternalStorageManager()) {
                        requestManageAllFilesLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        val wallpaperManager = WallpaperManager.getInstance(LocalContext.current)
                        val wallpaperBitmap = (wallpaperManager.drawable as? BitmapDrawable)?.bitmap?.asImageBitmap()
                        wallpaperBitmap?.apply {
                            Image(
                                bitmap = this,
                                contentDescription = "System Wallpaper",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        openAllFilesAccessSettings()

                    }
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

    private fun requestWallpaperPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE  // Android 13+: 모든 파일 접근 (Google Play 제한 주의)
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE  // Android 12 이하
        }

        // 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            requestManageAllFilesLauncher.launch(permission)
        }
    }

    private fun openAllFilesAccessSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)  // 앱 패키지 이름
        startActivity(intent)
    }
}
