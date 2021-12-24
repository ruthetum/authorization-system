package com.example.userservice.controller;

import com.example.userservice.dto.response.UserListResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service/admin/api")
@Slf4j
public class AdminController {

    private final UserService userService;

    /**
     * 사용자 목록
     */
    @GetMapping("/users")
    public ResponseEntity<UserListResponse> getUsers(
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        log.info("/admin/api/users?page={}", page);
        UserListResponse userListResponse = new UserListResponse(
            userService.countNormalUser(),
            userService.getUsersByPage(page)
        );
        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }
}
