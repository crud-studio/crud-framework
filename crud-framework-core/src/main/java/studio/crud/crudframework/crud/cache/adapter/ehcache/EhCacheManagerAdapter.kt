package studio.crud.crudframework.crud.cache.adapter.ehcache

import net.sf.ehcache.Cache
import net.sf.ehcache.CacheManager
import net.sf.ehcache.config.CacheConfiguration
import org.slf4j.LoggerFactory
import studio.crud.crudframework.crud.cache.CacheManagerAdapter
import studio.crud.crudframework.crud.cache.CrudCache
import studio.crud.crudframework.crud.cache.CrudCacheOptions

class EhCacheManagerAdapter(
    private val cacheManager: CacheManager
) : CacheManagerAdapter {
    override fun getCache(name: String): CrudCache? {
        val vendorCache = cacheManager.getCache(name) ?: return null
        return CrudEhCacheImpl(vendorCache)
    }

    override fun createCache(name: String, options: CrudCacheOptions): CrudCache {
        log.debug("Attempting to create cache with name [ $name ] and options [ $options ]")
        val configuration = CacheConfiguration(name, 0)
        val (timeToLiveSeconds, timeToIdleSeconds, maxEntries) = options
        if (timeToLiveSeconds != null) {
            configuration.timeToLiveSeconds = timeToLiveSeconds
        }

        if (timeToIdleSeconds != null) {
            configuration.timeToIdleSeconds = timeToIdleSeconds
        }

        if (maxEntries != null) {
            configuration.maxEntriesLocalHeap = maxEntries
        }

        val vendorCache = cacheManager.addCacheIfAbsent(Cache(configuration))
        log.debug("Created cache with [ $name ] and options [ $options ]")
        return CrudEhCacheImpl(vendorCache)
    }

    companion object {
        private val log = LoggerFactory.getLogger(EhCacheManagerAdapter::class.java)
    }
}