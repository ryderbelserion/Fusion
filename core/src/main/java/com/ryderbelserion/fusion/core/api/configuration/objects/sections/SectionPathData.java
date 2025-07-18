package com.ryderbelserion.fusion.core.api.configuration.objects.sections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public final class SectionPathData {

    private List<String> inlineComments;
    private List<String> comments;
    private Object data;

    public SectionPathData(@Nullable final Object data) {
        this.inlineComments = Collections.emptyList();
        this.comments = Collections.emptyList();
        this.data = data;
    }

    public void setData(@Nullable final Object data) {
        this.data = data;
    }

    public @Nullable Object getData() {
        return this.data;
    }

    public void setComments(@Nullable final List<String> comments) {
        this.comments = (comments == null) ? Collections.emptyList() : Collections.unmodifiableList(comments);
    }

    public @NotNull List<String> getComments() {
        return this.comments;
    }

    public void setInlineComments(@Nullable final List<String> inlineComments) {
        this.inlineComments = (inlineComments == null) ? Collections.emptyList() : Collections.unmodifiableList(inlineComments);
    }

    public @NotNull List<String> getInlineComments() {
        return this.inlineComments;
    }
}