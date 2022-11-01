package com.nowiczenko.andrzej.biblioteka.utils


sealed class StateEvent{
    class Success<T>(val result: T): StateEvent()
    class Failure(val error: String): StateEvent()
    object Loading: StateEvent()
    object Empty: StateEvent()
}
