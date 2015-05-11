package io.zarda.elnerd.model;

/**
 * Created by Ahmed Atef on 11/05/15.
 */
public class Constants {
    public static final int DB_REQUEST_COUNT = 2;
    public static final String API_URL = "http://elnerd.zarda.io/api";

    public enum SharedMemory {
        NAME("shared_memory"),
        LAST_SYNC_TIMESTAMP("last_sync_timestamp");

        private String name;

        SharedMemory(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }

        public enum Score {
            LONGEST_PLAYED,
            ALL_PLAYED;

            @Override
            public String toString() {
                return super.toString().toLowerCase();
            }
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
    }
}
