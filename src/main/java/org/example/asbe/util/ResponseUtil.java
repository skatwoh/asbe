package org.example.asbe.util;

import jakarta.servlet.http.HttpServletRequest;
import org.example.asbe.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class ResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

    // ✅ Phản hồi thành công (có dữ liệu và thông báo)
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return buildResponse(HttpStatus.OK, message, data, null);
    }

    // ✅ Phản hồi thành công (chỉ có dữ liệu, mặc định message là "Success")
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return buildResponse(HttpStatus.OK, "Success", data, null);
    }

    // ❌ Phản hồi lỗi (có status, message, không có dữ liệu)
    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message) {
        return buildResponse(status, message, null, null);
    }

    // ❌ Phản hồi lỗi (có status, message, kèm dữ liệu bổ sung)
    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, T data) {
        return buildResponse(status, message, data, null);
    }

    // ❌ Phản hồi lỗi (có status, message, danh sách lỗi chi tiết)
    public static ResponseEntity<ApiResponse<Void>> error(HttpStatus status, String message, List<String> errors) {
        return buildResponse(status, message, null, errors);
    }

    // ✅ Phản hồi chung (có thể là thành công hoặc lỗi)
    public static <T> ResponseEntity<ApiResponse<T>> response(HttpStatus status, String message, T data, List<String> errors) {
        return buildResponse(status, message, data, errors);
    }


    // 🚀 Hàm chung để xây dựng phản hồi API
    private static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus status, String message, T data, List<String> errors) {
        String path = getCurrentRequestPath();
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, data, path, errors);

        if (status.isError()) {
            log.error("API Error: status={}, message={}, path={}, traceId={}, errors={}",
                    status, message, path, response.getTraceId(), errors);
        }

        return ResponseEntity.status(status).body(response);
    }

    // 🔍 Lấy đường dẫn API hiện tại
    private static String getCurrentRequestPath() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        }
        return "Unknown";
    }
}