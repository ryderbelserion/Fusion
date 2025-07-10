package com.ryderbelserion.fusion.paper.files.types;

import com.ryderbelserion.fusion.core.api.enums.FileAction;
import com.ryderbelserion.fusion.core.api.enums.FileType;
import com.ryderbelserion.fusion.core.api.interfaces.files.ICustomFile;
import com.ryderbelserion.fusion.core.api.utils.FileUtils;
import org.jetbrains.annotations.NotNull;
import java.nio.file.Path;
import java.util.List;

public class NbtCustomFile extends ICustomFile<NbtCustomFile> {

    /**
     * Constructs a {@code NbtCustomFile} with the specified file path, actions.
     *
     * @param path    the file path associated with the configuration file
     * @param actions the list of file actions applied to the configuration file
     */
    public NbtCustomFile(@NotNull final Path path, @NotNull final List<FileAction> actions) {
        super(path, actions);
    }

    /**
     * Extracts the nbt file.
     *
     * @return {@link NbtCustomFile}
     */
    @Override
    public @NotNull final NbtCustomFile load() {
        if (getActions().contains(FileAction.MANUALLY_SAVED)) return this; // returns if manually saved, file is not in src/main, so duh

        FileUtils.extract(getFileName(), this.fusion.getPath(), getActions());

        return this;
    }

    /**
     * Retrieves the file type.
     *
     * @return the {@link FileType}
     */
    @Override
    public @NotNull final FileType getFileType() {
        return FileType.NBT;
    }
}