package com.ksucapstone.gasandgo.Repositories;

import android.util.LruCache;

public class MemoryCache
{
    private static LruCache<String, Object> mMemoryCache;

    public static MemoryCache GetInstance()
    {
        return new MemoryCache();
    }

    private MemoryCache()
    {
        if(mMemoryCache == null)
        {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<>(cacheSize);
        }
    }

    public void put(String key, Object value)
    {
        mMemoryCache.put(key, value);
    }

    public Object get(String key)
    {
        return mMemoryCache.get(key);
    }

    public void remove(String key)
    {
        mMemoryCache.remove(key);
    }
}