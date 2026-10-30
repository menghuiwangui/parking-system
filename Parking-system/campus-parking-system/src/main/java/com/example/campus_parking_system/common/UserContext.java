package com.example.campus_parking_system.common;

public class UserContext {
    private static final ThreadLocal<Integer> userId = new ThreadLocal<>();
    private static final ThreadLocal<Integer> role = new ThreadLocal<>();   // 0普通 1管理员

    public static void set(Integer id, Integer r) { userId.set(id); role.set(r); }
    public static Integer getUserId() { return userId.get(); }
    public static Integer getRole() { return role.get(); }
    public static void clear() { userId.remove(); role.remove(); }
}
