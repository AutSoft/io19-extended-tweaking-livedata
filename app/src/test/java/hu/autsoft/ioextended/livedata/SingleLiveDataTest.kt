@file:Suppress("UsePropertyAccessSyntax")

package hu.autsoft.ioextended.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import hu.autsoft.ioextended.livedata.util.LifecycleTest
import hu.autsoft.ioextended.livedata.util.MockObserver
import org.junit.Before
import org.junit.Test

class SingleLiveDataTest : LifecycleTest() {

    private val liveData: MutableLiveData<String> = SingleLiveData()

    private val mockObserver = MockObserver<String>()

    @Before
    fun setUp() {
        liveData.observe(this, mockObserver)
    }

    @Test(expected = IllegalStateException::class)
    fun noMultipleObservers() {
        liveData.observe(this, Observer {})
    }

    @Test
    fun dispatchedJustOnce() {
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        liveData.setValue("a")
        liveData.setValue("b")
        mockObserver.assertObserved("a", "b")

        liveData.removeObserver(mockObserver)
        liveData.observe(this, mockObserver)
        mockObserver.assertObservedNothing()
    }

    private fun MockObserver<*>.assertObservedNothing() {
        assertObserved()
    }

}
