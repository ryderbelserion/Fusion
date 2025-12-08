package com.ryderbelserion.fusion.kyori.permissions;

import com.ryderbelserion.fusion.core.FusionProvider;
import com.ryderbelserion.fusion.core.api.interfaces.permissions.IPermissionRegistry;
import com.ryderbelserion.fusion.kyori.FusionKyori;
import com.ryderbelserion.fusion.kyori.permissions.objects.Permission;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionRegistry implements IPermissionRegistry<Permission> {

    private final FusionKyori fusion = (FusionKyori) FusionProvider.getInstance();

    private final Map<String, List<Permission>> permissions = new HashMap<>();

    @Override
    public void start() {
        for (final Map.Entry<String, List<Permission>> values : this.permissions.entrySet()) {
            final List<Permission> permissions = values.getValue();

            final String name = values.getKey();

            for (final Permission permission : permissions) {
                permission.init();

                this.fusion.log("warn", "Registering the permission {} for {} with {} children.", name, permission.getNode(), permission.getChildren());
            }
        }
    }

    @Override
    public void addPermissions(@NotNull final String namespace, @NotNull final List<Permission> permissions) {
        for (final Permission permission : permissions) {
            addPermission(namespace, permission);
        }
    }

    @Override
    public void addPermission(@NotNull final String namespace, @NotNull final Permission permission) {
        if (!this.permissions.containsKey(namespace)) {
            this.permissions.put(namespace, new ArrayList<>());
        }

        this.permissions.get(namespace).add(permission);
    }
}