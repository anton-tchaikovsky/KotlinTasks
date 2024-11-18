package com.example.kotlintasks.navigation_router.presentation

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.kotlintasks.KotlinTasksApp
import com.example.kotlintasks.R
import com.example.kotlintasks.navigation_router.navigation.Navigator
import com.example.kotlintasks.navigation_router.navigation.NavigatorImpl
import com.example.kotlintasks.navigation_router.navigation.Router
import com.example.kotlintasks.work_manager.ChargingWork
import java.util.concurrent.TimeUnit

class NavigationActivity : AppCompatActivity(), NavigationRequestListener {

    private val router: Router = KotlinTasksApp.instance.router

    private val navigator: Navigator =
        NavigatorImpl(supportFragmentManager, R.id.fragment_container)

    // контракт для запроса разрешений на POST_NOTIFICATIONS
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { grande ->
            if (grande) initWorkCharging()
            else {
                // срабатывает после "отмена" с галочкой "больше не спрашивать"
                if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS))
                    Toast.makeText(
                        this,
                        getString(R.string.no_permission_post_notification),
                        Toast.LENGTH_SHORT
                    ).show()
                // срабатывает после "отмена" без галочки "больше не спрашивать"
                else
                    Toast.makeText(
                        this,
                        getString(R.string.no_permission_post_notification),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private val workManager = WorkManager.getInstance(this)

    private val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .build()

    private val workChargingRequest =
        PeriodicWorkRequestBuilder<ChargingWork>(15, TimeUnit.MINUTES)
            .setInitialDelay(CHARGING_WORK_INITIAL_DELAY, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navigation)
        router.setNavigator(navigator)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else initWorkCharging()
        if (savedInstanceState == null)
            router.start()
    }

    override fun onBack() {
        router.back()
    }

    override fun onNext() {
        router.next()
    }

    override fun onDestroy() {
        router.removeNavigator()
        super.onDestroy()
    }

    private fun initWorkCharging() {
        workManager.enqueueUniquePeriodicWork(
            UNIQUE_CHARGING_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            workChargingRequest
        )
    }

    companion object {
        const val UNIQUE_CHARGING_WORK_NAME = "unique charging work name"
        const val CHARGING_WORK_INITIAL_DELAY = 2000L
    }
}