package com.ryderbelserion.paper.builder.gui.interfaces.types;

import com.ryderbelserion.paper.builder.gui.interfaces.GuiItem;
import com.ryderbelserion.paper.builder.gui.types.PaginatedGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IPaginatedGui {

    PaginatedGui setPageSize(final int pageSize);

    void addItem(@NotNull final GuiItem guiItem);

    void removePageItem(final int slot);

    void updatePageItem(final int slot, @NotNull final ItemStack itemStack);

    void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack);

    void updatePageItem(final int row, final int col, @NotNull final GuiItem guiItem);

    void updatePageItem(final int slot, @NotNull final GuiItem guiItem);

    void open(@NotNull final Player player, @NotNull final Consumer<PaginatedGui> consumer);

    void open(@NotNull final Player player, final int openPage, @Nullable final Consumer<PaginatedGui> consumer);

    int getNextPageNumber();

    int getPreviousPageNumber();

    boolean next();

    boolean previous();

    GuiItem getPageItem(final int slot);

    void setPageNumber(final int pageNumber);

    int getCurrentPageNumber();

    List<GuiItem> getItemsFromPage(final int givenPage);

    Map<Integer, GuiItem> getCurrentPageItems();

    void clearPageContents();

    void clearPageItems(final boolean update);

    void clearPageItems();

    int getMaxPages();

    void populatePage();

    void updatePage();

    int calculatePageSize();

}