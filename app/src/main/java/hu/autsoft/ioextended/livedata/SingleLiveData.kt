package hu.autsoft.ioextended.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A LiveData implementation that accepts only a single Observer,
 * and only delivers a given value once.
 */
open class SingleLiveData<T : Any> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)

    private var currentObserver: Observer<in T>? = null
    private var observerWrapper: Observer<in T>? = null

    @MainThread
    override fun observe(owner: LifecycleOwner,
                         observer: Observer<in T>) {
        if (currentObserver != null) {
            throw IllegalStateException("!!!")
        }

        currentObserver = observer

        val wrapper = Observer<T> { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
        observerWrapper = wrapper

        super.observe(owner, wrapper)
    }

    override fun removeObserver(observer: Observer<in T>) {
        val isCurrentObserver = observer === currentObserver
        val isObserverWrapper = observer === observerWrapper

        if (isCurrentObserver || isObserverWrapper) {
            observerWrapper?.let { super.removeObserver(it) }

            currentObserver = null
            observerWrapper = null
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

}
