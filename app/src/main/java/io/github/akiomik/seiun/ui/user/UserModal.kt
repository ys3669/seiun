package io.github.akiomik.seiun.ui.user

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import io.github.akiomik.seiun.SeiunApplication
import io.github.akiomik.seiun.model.app.bsky.actor.Profile
import io.github.akiomik.seiun.ui.theme.Indigo800

@Composable
private fun UserBanner(profile: Profile, height: Dp = 128.dp) {
    if (profile.banner == null) {
        Box(
            modifier = Modifier
                .background(color = Indigo800)
                .height(height)
                .fillMaxWidth()
        ) {}
    } else {
        AsyncImage(
            model = profile.banner,
            contentDescription = null,
            modifier = Modifier.height(height),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun Avatar(profile: Profile) {
    AsyncImage(
        model = profile.avatar,
        contentDescription = null,
        modifier = Modifier
            .width(60.dp)
            .height(60.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun NameAndHandle(profile: Profile) {
    val handle = "@${profile.handle}"

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = profile.displayName ?: handle,
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = handle,
            color = Color.Gray,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun Profile(profile: Profile) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(profile = profile)
            NameAndHandle(profile = profile)
        }

        Text(profile.description.orEmpty())
    }
}

@Composable
private fun UserModalContent(profile: Profile) {
    SideEffect {
        Log.d(SeiunApplication.TAG, profile.toString())
    }

    Column {
        UserBanner(profile)
        Profile(profile)
        Divider(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        UserFeed(profile)
    }
}

@Composable
fun UserModal(profile: Profile, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            UserModalContent(profile = profile)
        }
    }
}
