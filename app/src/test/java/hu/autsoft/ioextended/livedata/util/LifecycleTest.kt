package hu.autsoft.ioextended.livedata.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.Rule
import org.junit.rules.TestRule

abstract class LifecycleTest : LifecycleOwner {
    @JvmField
    @Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    protected val lifecycle = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycle
    }
}
