package com.ymyang.generator.config;

import java.util.List;

public class Modules {
        private String name;
        private List<String> tables;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTables() {
            return tables;
        }

        public void setTables(List<String> tables) {
            this.tables = tables;
        }
    }