package com.ryderbelserion.fusion.core.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.files.CustomFile;
import java.io.File;

public class NbtCustomFile extends CustomFile<NbtCustomFile> {

    public NbtCustomFile(final File file, final boolean isDynamic) {
        super(file, isDynamic, ".nbt");
    }

    @Override
    public final NbtCustomFile loadConfiguration() {
        return this;
    }

    @Override
    public final NbtCustomFile saveConfiguration() {
        return this;
    }

    @Override
    public final NbtCustomFile getInstance() {
        return this;
    }

    @Override
    public final FileType getFileType() {
        return FileType.NBT;
    }
}