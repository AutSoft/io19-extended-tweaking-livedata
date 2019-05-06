package hu.autsoft.ioextended.livedata.util

import androidx.lifecycle.Observer
import org.junit.Assert
import org.junit.Assert.assertEquals
import java.util.ArrayList
import java.util.Arrays

class MockObserver<T> : Observer<T> {
    private val observed: MutableList<T?> = ArrayList()

    override fun onChanged(t: T?) {
        observed.add(t)
    }

    fun assertObserved(vararg expected: T?) {
        assertEquals(expected.asList(), observed)
        observed.clear()
    }
}
