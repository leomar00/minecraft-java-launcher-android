package com.minecraft.launcher.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.minecraft.launcher.R
import com.minecraft.launcher.domain.model.MinecraftVersion
import com.minecraft.launcher.presentation.viewmodel.VersionsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfilesScreen(
    viewModel: VersionsViewModel = koinViewModel()
) {
    val versionsState by viewModel.versionsState.collectAsState()
    val availableVersions by viewModel.availableVersions.collectAsState()
    val installedVersions by viewModel.installedVersions.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()

    var showInstallDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadVersions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.profiles),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            FloatingActionButton(
                onClick = { showInstallDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.profiles),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        // Tab Row
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Installed (${installedVersions.size})") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Available") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content
        when (versionsState) {
            VersionsViewModel.VersionsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is VersionsViewModel.VersionsState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (versionsState as VersionsViewModel.VersionsState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            VersionsViewModel.VersionsState.Installing -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator(
                        progress = { downloadProgress },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${(downloadProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            else -> {
                when (selectedTab) {
                    0 -> InstalledVersionsList(installedVersions, viewModel)
                    1 -> AvailableVersionsList(availableVersions, viewModel)
                }
            }
        }
    }
}

@Composable
fun InstalledVersionsList(
    versions: List<MinecraftVersion>,
    viewModel: VersionsViewModel
) {
    if (versions.isEmpty()) {
        EmptyProfilesMessage()
    } else {
        LazyColumn {
            items(versions) { version ->
                VersionCard(version, onDelete = {
                    viewModel.uninstallVersion(version.id)
                })
            }
        }
    }
}

@Composable
fun AvailableVersionsList(
    versions: List<MinecraftVersion>,
    viewModel: VersionsViewModel
) {
    LazyColumn {
        items(versions) { version ->
            VersionCard(version, showInstall = true, onInstall = {
                viewModel.installVersion(version)
            })
        }
    }
}

@Composable
fun VersionCard(
    version: MinecraftVersion,
    showInstall: Boolean = false,
    onInstall: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = version.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${version.sizeInMB.toInt()} MB",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (showInstall && onInstall != null) {
                    Button(onClick = onInstall) {
                        Text(stringResource(R.string.install_version))
                    }
                } else if (!showInstall && onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyProfilesMessage() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📦",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_versions_installed),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(stringResource(R.string.install_version))
            }
        }
    }
}
