package com.example.modu.service;

import com.example.modu.dto.TestElement.TestsResponseDto;
import com.example.modu.dto.user.*;
import com.example.modu.entity.TestElement.Tester;
import com.example.modu.entity.TestElement.UserTestResult;
import com.example.modu.entity.User;
import com.example.modu.entity.UserRoleEnum;
import com.example.modu.repository.TesterRepository;
import com.example.modu.repository.UserRepository;
import com.example.modu.repository.UserTestResultRepository;
import com.example.modu.util.JwtUtil;
import com.example.modu.util.S3Config;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@Slf4j(topic = "User Service")
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TesterRepository testerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserTestResultRepository userTestResultRepository;
    private final S3Config s3Config;
    private final JwtUtil jwtUtil;

    public String cryptPassword(String password)
    {
        String pattern = "^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        if (!Pattern.matches(pattern, password))
        {
            throw  new PatternSyntaxException("비밀 번호 조건에 부합 되지 않음", pattern, -1);
        }

        return passwordEncoder.encode(password);
    }

    public ResponseEntity<StatusResponseDto> signup(SignupRequestDto requestDto) throws IOException {
        Optional<User> checkUsername = userRepository.findByUsername(requestDto.getUsername());
        if (checkUsername.isPresent())
            throw new IllegalArgumentException("중복된 사용자가 존재 합니다.");
        
        String cryptPassword = cryptPassword(requestDto.getPassword());


        User user = new User(requestDto.getUsername(), requestDto.getEmail(), cryptPassword,
                s3Config.upload(requestDto.getImage()), requestDto.getNickname());
        userRepository.save(user);
        
        return ResponseEntity.ok(new StatusResponseDto("회원가입 성공" , 200));
    }


    public  ResponseEntity<StatusResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response)
    {
        Optional<User> target = userRepository.findByUsername(requestDto.getUsername());
        if (target.isEmpty())
            throw new IllegalArgumentException("사용자가 존재 하지 않습니다.");

        if (! passwordEncoder.matches(requestDto.getPassword(), target.get().getPassword()))
        {
            throw new IllegalArgumentException("틀린 비밀번호");
        }

        String token = jwtUtil.createToken(requestDto.getUsername(), UserRoleEnum.USER);
        //jwtUtil.addJwtToCookie(token, response); // 토큰은 도메인이 같아야 저장
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);// 어느 요청이든 저장 , 로컬 스토리지에 저장

        return ResponseEntity.ok(new StatusResponseDto("로그인 성공", 200));
    }


    public ResponseEntity<UserDataResponse> myPage(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");
        return ResponseEntity.ok(new UserDataResponse(user.getId(), user.getNickname()));
    }

    public  ResponseEntity<List<TestsResponseDto>> makedTests(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

       return ResponseEntity.ok(testerRepository.findAllByUser(user).stream().map(TestsResponseDto::new).toList());
    }
    public ResponseEntity<StatusResponseDto> update(User user,
                                                    UserUpdateRequestDto updateValue)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

        user.Update(updateValue.getNickname(), cryptPassword(updateValue.getPassword()));
        
        return ResponseEntity.ok(new StatusResponseDto("변경 완료", 200));
    }
    public ResponseEntity<StatusResponseDto> deleteUser(User user)
    {
        if (user == null)
            throw new IllegalArgumentException("인증 되지 않은 유저");

        userRepository.deleteById(user.getId());
        return ResponseEntity.ok(new StatusResponseDto("회원 탈퇴 완료", 200));
    }

    public ResponseEntity<List<TestsResponseDto>> getJoinTests(User user)
    {
        List<UserTestResult> testResults = userTestResultRepository.findAllByUser_Id(user.getId());
        TestsResponseDto[] Results = new TestsResponseDto[testResults.size()];//속도 때문에
        for(int i = 0; i < testResults.size(); i++ )
        {
            Results[i] = new TestsResponseDto(testResults.get(i).getTester());
        }
        return ResponseEntity.ok(Arrays.stream(Results).toList());
    }
    public ResponseEntity<StatusResponseDto> updateProfile(User user, MultipartFile multipartFile) throws IOException {
        if (!user.getImage().isEmpty())
        {
            //s3Config.
            //============ 기존에 있는 이미지 제거
        }

        user.setImage(s3Config.upload(multipartFile));
        userRepository.save(user);
        return ResponseEntity.ok(new StatusResponseDto("변경 완료", 200));
    }

}
