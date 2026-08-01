package com.ryderbelserion.fusion.paper.files;

import com.ryderbelserion.fusion.files.FileManager;
import com.ryderbelserion.fusion.files.enums.FileAction;
import com.ryderbelserion.fusion.files.enums.FileType;
import com.ryderbelserion.fusion.paper.files.types.PaperCustomFile;
import org.jspecify.annotations.NullMarked;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

@NullMarked
public final class PaperFileManager extends FileManager {

    public PaperFileManager(final Path path) {
        super(path);
    }

    public PaperFileManager addPaperFile(final Path path, final Consumer<PaperCustomFile> consumer) {
        getPaperFile(path).ifPresentOrElse(customFile -> {
            consumer.accept(customFile);

            if (customFile.hasAction(FileAction.RELOAD_FILE)) {
                customFile.load();
            }
        }, () -> addPaperFile(new PaperCustomFile(this, path, consumer).load()));

        return this;
    }

    public PaperFileManager addPaperFile(final PaperCustomFile customFile) {
        addFile(customFile.getPath(), customFile);

        return this;
    }

    public PaperFileManager addPaperFolder(final Path folder, final String jarFolder, final Consumer<PaperCustomFile> consumer) {
        final FileType fileType = FileType.PAPER_YAML;

        extractFolder(folder.getFileName().toString(), jarFolder, fileType, folder.getParent());

        for (final Path path : getFilesByPath(folder, fileType.getExtension(), getDepth())) {
            addPaperFile(path, consumer);
        }

        return this;
    }

    public PaperFileManager addPaperFolder(final Path folder, final String jarFolder) {
        return addPaperFolder(folder, jarFolder, _ -> {});
    }

    public PaperFileManager addPaperFolder(final Path folder, final Consumer<PaperCustomFile> consumer) {
        return addPaperFolder(folder, "", consumer);
    }

    public PaperFileManager addPaperFolder(final Path folder) {
        return addPaperFolder(folder, consumer -> consumer.addAction(FileAction.EXTRACT_FOLDER));
    }

    public PaperFileManager addPaperFile(final Path path) {
        return addPaperFile(path, consumer -> consumer.addAction(FileAction.EXTRACT_FILE));
    }

    public PaperCustomFile buildPaperFile(final Path path, final Consumer<PaperCustomFile> consumer) {
        return new PaperCustomFile(this, path, consumer);
    }

    public PaperFileManager savePaperFile(final PaperCustomFile customFile) {
        customFile.save();

        return this;
    }

    public Optional<PaperCustomFile> getPaperFile(final Path path) {
        return getFile(path).map(PaperCustomFile.class::cast);
    }
}