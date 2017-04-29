package com.yalin.style.data.repository.datasource;


import android.content.Context;

import com.yalin.style.data.cache.WallpaperCache;

import com.yalin.style.data.lock.OpenInputStreamLock;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author jinyalin
 * @since 2017/4/18.
 */

@Singleton
public class WallpaperDataStoreFactory {

  private final Context context;
  private final WallpaperCache wallpaperCache;
  private final OpenInputStreamLock openInputStreamLock;

  @Inject
  WallpaperDataStoreFactory(Context context, WallpaperCache wallpaperCache,
      OpenInputStreamLock openInputStreamLock) {
    this.context = context;
    this.wallpaperCache = wallpaperCache;
    this.openInputStreamLock = openInputStreamLock;
  }

  public WallpaperDataStore create() {
    WallpaperDataStore wallpaperDataStore;
    if (!wallpaperCache.isDirty() && wallpaperCache.isCached()) {
      wallpaperDataStore = new CacheWallpaperDataStore(wallpaperCache, openInputStreamLock);
    } else {
      wallpaperDataStore = createDbDataStore();
    }
    return wallpaperDataStore;
  }

  public WallpaperDataStore createDbDataStore() {
    return new DbWallpaperDataStore(context, wallpaperCache, openInputStreamLock);
  }

  public void onDataRefresh() {
    wallpaperCache.evictAll();
  }
}
