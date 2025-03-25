package com.ryderbelserion.fusion.api.files.types.misc;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.files.CustomFile;
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