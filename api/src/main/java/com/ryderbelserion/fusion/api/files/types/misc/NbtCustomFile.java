package com.ryderbelserion.fusion.api.files.types.misc;

import com.ryderbelserion.fusion.api.enums.FileType;
import com.ryderbelserion.fusion.api.files.CustomFile;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class NbtCustomFile extends CustomFile<NbtCustomFile> {

    public NbtCustomFile(@NotNull final Path path, final boolean isDynamic) {
        super(path, isDynamic);
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