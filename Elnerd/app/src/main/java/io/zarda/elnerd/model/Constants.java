package io.zarda.elnerd.model;

/**
 * Created by Ahmed Atef on 11/05/15.
 */
public class Constants {
    public static final int DB_REQUEST_COUNT = 2;

    public enum SharedMemory {
        NAME ("shared_memory"),
        LAST_SYNC_TIMESTAMP ("last_sync_timestamp");

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
}
