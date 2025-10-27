package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.files.interfaces.ICustomFile;
import com.ryderbelserion.fusion.paper.files.PaperFileManager;
import com.ryderbelserion.fusion.paper.structure.StructureBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;

public class NbtCustomFile extends ICustomFile<NbtCustomFile, StructureBuilder, Object, Object> {

    private final JavaPlugin plugin;
    private StructureBuilder builder;

    public NbtCustomFile(@NotNull final JavaPlugin plugin, @NotNull final FileManager fileManager, @NotNull final Path path) {
        super(fileManager, path);

        this.plugin = plugin;
    }

    @Override
    public @NotNull final StructureBuilder getConfiguration() {
        return this.builder;
    }

    @Override
    public @NotNull final StructureBuilder loadConfig() {
        return this.builder = new StructureBuilder(this.plugin, (PaperFileManager) this.fileManager, this.path.getFileName().toString());
    }

    @Override
    public @NotNull final FileType getFileType() {
        return FileType.NBT;
    }
}