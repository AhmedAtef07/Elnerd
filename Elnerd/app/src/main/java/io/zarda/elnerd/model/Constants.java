package io.zarda.elnerd.model;

import java.lang.reflect.Type;

/**
 * Created by Ahmed Atef on 11/05/15.
 */
public class Constants {
    public static final int DB_REQUEST_COUNT = 2;
    public static final String API_URL = "http://elnerd.zarda.io/api";
    public static final String SHARED_MEMORY_NAME = "shared_memory";


    public enum SharedMemory {
        LAST_SYNC_TIMESTAMP("last_sync_timestamp", long.class),
        LONGEST_PLAYED("longest_played", int.class),
        ALL_PLAYED("all_played", int.class);

        private String name;
        private Type type;

        private SharedMemory(String name, Type type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public Type getType() {
            return type;
        }
    }

    public enum DB {
        NAME("elnerd.db");

        private String name;

        private DB(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public enum Questions {
            NAME("questions"),
            ID("id"),
            HEADER("header"),
            ANSWER_ID("answer_id"),
            QUOTE_ID("quote_id"),
            MODE_ID("mode_id"),
            VIEW_COUNTER("view_counter");

            private String name;

            private Questions(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        public enum Choices {
            NAME("choices"),
            ID("id"),
            HEADER("header"),
            QUESTION_ID("question_id");

            private String name;

            private Choices(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        public enum Modes {
            NAME("modes"),
            ID("id"),
            TITLE("title");

            private String name;

            private Modes(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        public enum Quotes {
            NAME("quotes"),
            ID("id"),
            CONTENT("content"),
            BOOK("book"),
            USER_ID("user_id");

            private String name;

            private Quotes(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return name;
            }
        }
    }
}
