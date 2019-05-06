@file:Suppress("UsePropertyAccessSyntax")

package hu.autsoft.ioextended.livedata

import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.MutableLiveData
import hu.autsoft.ioextended.livedata.util.LifecycleTest
import hu.autsoft.ioextended.livedata.util.MockObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class QueuedSingleLiveDataTest : LifecycleTest() {
    private val liveData: MutableLiveData<String> = QueuedSingleLiveData()
    private val observer = MockObserver<String>()

    @Before
    fun setUp() {
        liveData.observe(this, observer)
    }

    @Test
    fun queueWhileStopped() {
        liveData.setValue("a")
        liveData.setValue("b")
        liveData.setValue("c")
        observer.assertObserved()

        lifecycle.handleLifecycleEvent(ON_START)
        observer.assertObserved("a", "b", "c")
    }

    @Test
    fun startStopStart() {
        liveData.setValue("a")
        observer.assertObserved()

        lifecycle.handleLifecycleEvent(ON_START)
        observer.assertObserved("a")

        liveData.setValue("b")
        observer.assertObserved("b")

        liveData.setValue("c")
        lifecycle.handleLifecycleEvent(ON_STOP)
        liveData.setValue("d")
        liveData.setValue("e")
        observer.assertObserved("c")

        lifecycle.handleLifecycleEvent(ON_START)
        observer.assertObserved("d", "e")
    }

    @Test
    fun observeRemoveObserve() {
        lifecycle.handleLifecycleEvent(ON_START)
        liveData.setValue("a")
        observer.assertObserved("a")

        liveData.removeObserver(observer)
        liveData.setValue("c")
        liveData.setValue("d")
        observer.assertObserved()

        liveData.observe(this, observer)
        observer.assertObserved("c", "d")
    }

}
