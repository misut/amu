package amu

import android.content.pm.ResolveInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap

@Composable
fun AppGrid(apps: List<ResolveInfo>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),  // 4열 고정 그리드
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(apps) { app ->
            AppItem(app = app)
        }
    }
}

@Composable
fun AppItem(app: ResolveInfo) {
    val context = LocalContext.current
    val pm = context.packageManager

    Column(
        modifier = Modifier
            .clickable {
                val launchIntent = pm.getLaunchIntentForPackage(app.activityInfo.packageName)
                context.startActivity(launchIntent)
            }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = app.loadIcon(pm).toBitmap().asImageBitmap(),
            contentDescription = app.loadLabel(pm).toString(),
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = app.loadLabel(pm).toString(),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onBackground  // Material3 색상 적용
        )
    }
}
