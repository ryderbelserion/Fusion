package com.ryderbelserion.fusion.core.api.support;

import com.ryderbelserion.fusion.core.api.support.objects.ModKey;
import java.util.List;

public class ModSupport {
    
    public static final ModKey placeholder_api = ModKey.key("fusion", "PlaceholderAPI");

    public static final ModKey head_database =  ModKey.key("fusion", "HeadDatabase");
    
    public static final ModKey items_adder = ModKey.key("fusion", "ItemsAdder");
    
    public static final ModKey yard_watch =  ModKey.key("fusion", "YardWatch");
    
    public static final ModKey oraxen =  ModKey.key("fusion", "Oraxen");
    
    public static final ModKey nexo = ModKey.key("fusion", "Nexo");

    public static final List<ModKey> dependencies = List.of(
            placeholder_api,
            head_database,
            items_adder,
            yard_watch,
            oraxen,
            nexo
    );
}