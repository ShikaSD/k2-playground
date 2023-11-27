import app.test.AtomicReference

class Session<T>(val value: T)

@JvmInline
value class SessionMutex<T> private constructor(
    private val currentSessionHolder: AtomicReference<Session<T>?>
) {
    constructor(t: T) : this(AtomicReference(Session(t)))

    val currentSession: T?
        get() = currentSessionHolder.get()?.value
}

fun box(): String = SessionMutex("OK").currentSession!!