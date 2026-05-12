package com.ryderbelserion.fusion.kyori.commands.api.senders.results;

import org.jetbrains.annotations.NotNull;

public interface ValidationResult<E> {

    class Valid<E> implements ValidationResult<E> {}

    class Ignore<E> implements ValidationResult<E> {}

    record Invalid<E> (E message) implements ValidationResult<E> {

        public Invalid(@NotNull final E message) {
            this.message = message;
        }

        @Override
        public @NotNull E message() {
            return this.message;
        }
    }
}