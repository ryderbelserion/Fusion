package com.ryderbelserion.fusion.kyori.mods;

import com.ryderbelserion.fusion.core.api.FusionKey;
import java.util.List;

public class ModSupport {
    
    public static final FusionKey placeholder_api = FusionKey.key("fusion", "PlaceholderAPI");

    public static final FusionKey head_database =  FusionKey.key("fusion", "HeadDatabase");
    
    public static final FusionKey items_adder = FusionKey.key("fusion", "ItemsAdder");
    
    public static final FusionKey yard_watch =  FusionKey.key("fusion", "YardWatch");
    
    public static final FusionKey oraxen =  FusionKey.key("fusion", "Oraxen");
    
    public static final FusionKey nexo = FusionKey.key("fusion", "Nexo");

    public static final List<FusionKey> dependencies = List.of(
            placeholder_api,
            head_database,
            items_adder,
            yard_watch,
            oraxen,
            nexo
    );
}