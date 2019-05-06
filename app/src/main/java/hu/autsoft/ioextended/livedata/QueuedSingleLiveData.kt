package hu.autsoft.ioextended.livedata

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import java.util.LinkedList
import java.util.Queue

/**
 * A LiveData implementation that queues all values that are set or posted
 * to it while its observer is inactive. When its observer becomes active,
 * it is notified about all queued values immediately.
 *
 * Subclass of [SingleLiveData], therefore it may only have a single
 * observer and delivers the value set in it once.
 */
class QueuedSingleLiveData<T : Any> : SingleLiveData<T>() {

    private val queue: Queue<T> = LinkedList<T>()
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun postValue(value: T?) {
        handler.post { setValue(value) }
    }

    @MainThread
    override fun setValue(t: T?) {
        if (hasActiveObservers() && queue.isEmpty()) {
            super.setValue(t)
        } else {
            queue.add(t)
        }
    }

    override fun onActive() {
        var element = queue.poll()
        while (element != null) {
            super.setValue(element)
            element = queue.poll()
        }
    }

}
