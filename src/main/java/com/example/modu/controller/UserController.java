package com.example.modu.controller;

import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.dto.user.*;
import com.example.modu.service.UserService;
import com.example.modu.util.JwtUtil;
import com.example.modu.util.S3Config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "User Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final S3Config s3Config;
    private final JwtUtil jwtUtil;

    @GetMapping("/loginForm")
    private ResponseEntity<String> loginPage()
    {
        return ResponseEntity.badRequest().body("로그인 실패!");//---> 실패해야 뜸 , 성공시 하이재킹 해감
    }// @RestController , @RestControllerAdvice 둘중 하나 있으면 문자열로 리턴
    /*
    @GetMapping("/signupForm")
    private String signupPage()
    {
        return "signupForm";
    }*/
    @PostMapping("/signup")
    private ResponseEntity<StatusResponseDto> signup(@RequestBody SignupRequestDto signup) throws IOException {
        return userService.signup(signup);
    }
    @PostMapping("/login")
    private ResponseEntity<StatusResponseDto> login(@RequestBody LoginRequestDto login,
                                                    HttpServletResponse response)
    {
        return userService.login(login, response);
    }
    @GetMapping("/logout")
    private ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response)
    {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("Logout");
    }
    @PutMapping("/update")
    private ResponseEntity<StatusResponseDto> update(@RequestBody UserUpdateRequestDto update,
                                                     HttpServletRequest request)
    {
        return userService.update(jwtUtil.getUserFromHeader(request), update);
    }

    // +++ 프로필 사진 변경 API 추가
    @PutMapping("/update-profile")
    private ResponseEntity<StatusResponseDto> updateProfile(@RequestParam("images") MultipartFile multipartFile,
                                                            HttpServletRequest request) throws IOException {
        return userService.updateProfile(jwtUtil.getUserFromHeader(request),multipartFile);
    }
    
    @DeleteMapping("/delete")
    private ResponseEntity<StatusResponseDto> deleteUser(HttpServletRequest request)
    {
        return userService.deleteUser(jwtUtil.getUserFromHeader(request));
    }

    /*
    @PostMapping("/login")
    private ResponseEntity<StatusResponseDto> login(@RequestBody LoginRequestDto login) {
        return userService.login(login);
    }*///Security 가 처리


    @GetMapping("/mypage") //단순 페이지 이동이 아닌 초기 로드시 정보
    private ResponseEntity<UserDataResponse> myPage(HttpServletRequest request)
        //(@AuthenticationPrincipal User user)
    {
        return userService.myPage(jwtUtil.getUserFromHeader(request));
    }

    @GetMapping("/tests")
    private ResponseEntity<List<TestsResponseDto>> makedTests(HttpServletRequest request)
    {
        return userService.makedTests(jwtUtil.getUserFromHeader(request));
    }
    
    
    @GetMapping("/join")
    private ResponseEntity<List<TestsResponseDto>> joinTests(HttpServletRequest request)
    {
        return userService.getJoinTests(jwtUtil.getUserFromHeader(request));
    }

    //==========
    
    /*
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("images")MultipartFile multipartFile) throws IOException
    {
        return ResponseEntity.ok(s3Config.upload(multipartFile));
    }
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("FileName") String fileName) throws IOException {
        return s3Config.download(fileName);
    }
    */// 사용 예시

}
