package com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.types;

import com.ryderbelserion.fusion.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.fusion.paper.api.builders.gui.types.PaginatedGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IPaginatedGui {

    PaginatedGui setPageSize(int pageSize);

    void addItem(@NotNull GuiItem guiItem);

    void removePageItem(int slot);

    void updatePageItem(int slot, @NotNull ItemStack itemStack);

    void updatePageItem(int row, int col, @NotNull ItemStack itemStack);

    void updatePageItem(int row, int col, @NotNull GuiItem guiItem);

    void updatePageItem(int slot, @NotNull GuiItem guiItem);

    void open(@NotNull Player player, @NotNull Consumer<PaginatedGui> consumer);

    void open(@NotNull Player player, int openPage, @Nullable Consumer<PaginatedGui> consumer);

    int getNextPageNumber();

    int getPreviousPageNumber();

    boolean next();

    boolean previous();

    @Nullable GuiItem getPageItem(int slot);

    void setPageNumber(int pageNumber);

    int getCurrentPageNumber();

    @NotNull List<GuiItem> getItemsFromPage(int givenPage);

    @NotNull Map<Integer, GuiItem> getCurrentPageItems();

    void clearPageContents();

    void clearPageItems(boolean update);

    void clearPageItems();

    int getMaxPages();

    void populatePage();

    void updatePage();

    int calculatePageSize();

}