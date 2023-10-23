package io.github.dracula101.todo.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.dracula101.todo.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit = {}
) {
    val displayPic = profileViewModel.currentUser?.profilePic
    val completedTask = profileViewModel.completedTask.collectAsState(initial = 0)
    val unCompletedTask = profileViewModel.unCompletedTask.collectAsState(initial = 0)
    val scrollState= rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier.verticalScroll(scrollState),
    ){
        Spacer(modifier = modifier.size(20.dp))
        Box(
            modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {}
                    .background(
                        MaterialTheme.colorScheme.inverseOnSurface

                    )
            ) {
                displayPic.let {
                    if (it != null) {
                        AsyncImage(
                            model = it,
                            modifier = modifier.fillMaxSize(),
                            contentDescription = "Profile Picture",
                            error = painterResource(id = R.drawable.user),
                        )
                    }
                }
            }
        }
        Spacer(modifier = modifier.size(8.dp))
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = profileViewModel.currentUser?.name ?: "User",
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier.padding(top = 10.dp),
            )
            Text(
                text = profileViewModel.currentUser?.email ?: "Email",
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(top = 10.dp),
            )
            Spacer(modifier = modifier.size(20.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            MaterialTheme.colorScheme.inverseOnSurface
                        )
                        .padding(
                            16.dp
                        )
                ) {
                    Text(
                        text = "${unCompletedTask.value} Tasks left",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                Box(
                    modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            MaterialTheme.colorScheme.inverseOnSurface
                        )
                        .padding(
                            16.dp
                        )
                ) {
                    Text(
                        text = "${completedTask.value} Tasks done",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Spacer(modifier = modifier.size(20.dp))
            Text(
                modifier = modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start),
                text = "Settings",
                color = MaterialTheme.colorScheme.primary,
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("App Settings")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Settings,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            Divider()
            Spacer(modifier = modifier.size(20.dp))
            Text(
                modifier = modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start),
                text = "Account",
                color = MaterialTheme.colorScheme.primary,
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("Change user account")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Person,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("Change account password")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Lock,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("Change account image")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Face,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            Divider()
            Spacer(modifier = modifier.size(20.dp))
            Text(
                modifier = modifier
                    .padding(start = 16.dp)
                    .align(Alignment.Start),
                text = "UpTodo",
                color = MaterialTheme.colorScheme.primary,
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("About us")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Email,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("FAQ")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.List,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { },
                headlineContent = {
                    Text("Help and Feedback")
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Create,
                        null
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            Divider()
            ListItem(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable {
                       onLogout()
                    },
                headlineContent = {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                },
                leadingContent = {
                    Icon(
                        Icons.Rounded.Lock,
                        null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Rounded.KeyboardArrowRight,
                        null
                    )
                }
            )
            Spacer(modifier = modifier.size(20.dp))
            Box(
                //add opacity to the whole
                modifier = modifier.fillMaxWidth().grayScale(),
                contentAlignment = Alignment.TopCenter
            ){
                Row (
                    modifier = modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surface
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ){
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = "Logo",
                        modifier = modifier.width(100.dp).padding(16.dp).alpha(0.5f)
                    )
                    Column(
                        modifier = modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "UpTodo",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Version 1.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            }

        }
    }

}

class GrayScaleModifier : DrawModifier {
    override fun ContentDrawScope.draw() {
        val saturationMatrix = ColorMatrix().apply { setToSaturation(0f) }

        val saturationFilter = ColorFilter.colorMatrix(saturationMatrix)
        val paint = Paint().apply {
            colorFilter = saturationFilter
        }
        drawIntoCanvas {
            it.saveLayer(Rect(0f, 0f, size.width, size.height), paint)
            drawContent()
            it.restore()
        }
    }
}

fun Modifier.grayScale() = this.then(GrayScaleModifier())
