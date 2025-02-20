package com.ryderbelserion.fusion.core.files.types.misc;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.files.CustomFile;
import org.jetbrains.annotations.NotNull;
import java.io.File;

public class NbtCustomFile extends CustomFile<NbtCustomFile> {

    public NbtCustomFile(@NotNull final File file, final boolean isDynamic) {
        super(file, isDynamic);
    }

    @Override
    public NbtCustomFile load() {
        return this;
    }

    @Override
    public NbtCustomFile save() {
        return this;
    }

    @Override
    public FileType getType() {
        return FileType.NBT;
    }
}