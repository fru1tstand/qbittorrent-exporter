package me.fru1t.qbtexporter.kotlin.impl

import me.fru1t.qbtexporter.kotlin.LazyRelay
import me.fru1t.qbtexporter.kotlin.LazyRelayFactory
import javax.inject.Inject

/** Implementation of [LazyRelayFactory]. */
class LazyRelayFactoryImpl @Inject constructor(): LazyRelayFactory {
  override fun <T> create(signal: () -> Any?, calculate: () -> T): LazyRelay<T> =
      LazyRelayImpl(signal, calculate)
}
