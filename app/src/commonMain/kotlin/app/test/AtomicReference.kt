package app.test

expect class AtomicReference<V>(value: V) {
    fun get(): V
    fun set(value: V)
    fun getAndSet(value: V): V
    fun compareAndSet(expect: V, newValue: V): Boolean
}