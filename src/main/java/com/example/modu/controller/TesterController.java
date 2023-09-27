package com.example.modu.controller;

import com.example.modu.dto.TestElement.TestDetailResponseDto;
import com.example.modu.dto.TestElement.TestMakeRequestDto;
import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.dto.result.ResultResponseDto;
import com.example.modu.dto.user.StatusResponseDto;
import com.example.modu.entity.User;
import com.example.modu.service.TesterService;
import com.example.modu.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TesterController {

    private final TesterService testerService;
    private  final JwtUtil jwtUtil;
    /*
    // 테스트 만들기 폼 페이지
    @GetMapping("/test/testMakeForm")
    public String testMakeForm(){
        return "testMakeForm";
    }
    */

    // 테스트 만들기
    @PostMapping("/test/testMakeForm")
    public ResponseEntity<StatusResponseDto> createTester(@RequestBody TestMakeRequestDto requestDto,
                                                          HttpServletRequest request) throws IOException {
        return testerService.createTester(requestDto, jwtUtil.getUserFromToken());
    }

    // 테스트 조회
    @GetMapping("/tests")
    public List<TestsResponseDto> getAllTests(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "false") boolean sortByCreatedAt) {
        return testerService.getAllTests(page, size, sortByCreatedAt);
    }

    // 카테고리별 테스트 조회
    @GetMapping("/tests/{category}")
    public List<TestsResponseDto> getTestsByCategory(@PathVariable String category,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size){
        return testerService.getTestsByCategory(category, page, size);
    }

    // 테스트 상세 조회
    @GetMapping("/test/{testId}")
    public TestDetailResponseDto getTestById(@PathVariable Long testId) {
        return testerService.getTestById(testId);
    }

    // 테스트 삭제
    @DeleteMapping("/test/{testId}")
    public ResponseEntity<StatusResponseDto> deleteTester(@PathVariable Long testId,
                                                          HttpServletRequest request){
        return testerService.deleteTester(testId, jwtUtil.getUserFromToken());
    }


    @PostMapping("/test/like")
    public ResponseEntity<StatusResponseDto> likeTester(
                                                        HttpServletRequest request)
    {
        return testerService.likeTest(jwtUtil.getUserFromToken());
    }
}
