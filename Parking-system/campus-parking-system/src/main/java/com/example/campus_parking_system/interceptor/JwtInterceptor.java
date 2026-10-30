package com.example.campus_parking_system.interceptor;

import com.example.campus_parking_system.annotation.RequireAdmin;
import com.example.campus_parking_system.common.UserContext;
import com.example.campus_parking_system.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handle) throws Exception {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 从请求头获取 token，格式: Authorization: Bearer xxx
        String auth = request.getHeader("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
            return false;
        }
        try {
            String token = auth.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            Integer userId = Integer.valueOf(claims.get("userId").toString());
            Integer role = Integer.valueOf(claims.get("role").toString());
            // 写入 ThreadLocal，后续业务可通过 UserContext 获取
            UserContext.set(userId,role);
            if (handle instanceof org.springframework.web.method.HandlerMethod handlerMethod) {
                // @RequireAdmin 校验
                if (handlerMethod.hasMethodAnnotation(RequireAdmin.class) || handlerMethod.getBeanType().isAnnotationPresent(RequireAdmin.class)) {
                    if (role != 1) {
                        response.setStatus(403);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("{\"code\":403,\"msg\":\"无权限访问\"}");
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token无效或已过期\"}");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response,Object handler,Exception ex) {
        // 请求结束清理 ThreadLocal，防止内存泄漏
        UserContext.clear();
    }
}
