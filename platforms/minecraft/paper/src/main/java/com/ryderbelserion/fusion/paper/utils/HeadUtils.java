package com.ryderbelserion.fusion.paper.utils;

import com.ryderbelserion.fusion.core.api.exceptions.FusionException;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class HeadUtils {

    private HeadDatabaseAPI api = null;

    public void init() {
        if (this.api == null) {
            this.api = new HeadDatabaseAPI();
        }
    }

    public final HeadDatabaseAPI getApi() {
        if (this.api == null) {
            throw new FusionException("HeadDatabaseAPI is not initialized.");
        }

        return this.api;
    }
}