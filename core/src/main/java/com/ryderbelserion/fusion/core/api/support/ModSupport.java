package com.ryderbelserion.fusion.core.api.support;

import net.kyori.adventure.key.Key;
import java.util.List;

public class ModSupport {
    
    public static final Key placeholder_api = Key.key("fusion", "PlaceholderAPI");

    public static final Key head_database =  Key.key("fusion", "HeadDatabase");
    
    public static final Key items_adder = Key.key("fusion", "ItemsAdder");
    
    public static final Key yard_watch =  Key.key("fusion", "YardWatch");
    
    public static final Key oraxen =  Key.key("fusion", "Oraxen");
    
    public static final Key nexo = Key.key("fusion", "Nexo");

    public static final List<Key> dependencies = List.of(
            placeholder_api,
            head_database,
            items_adder,
            yard_watch,
            oraxen,
            nexo
    );
}