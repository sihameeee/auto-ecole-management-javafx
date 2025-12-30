package com.example.demo;


    public class Session {

        public static String username;
        private static String role;

        public static String getUsername() {
            return username;
        }

        public static void setUsername(String username) {
            Session.username = username;
        }

        public static String getRole() {
            return role;
        }

        public static void setRole(String role) {
            Session.role = role;
        }
    }


