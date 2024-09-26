package com.salsa.lottery.service.impl;

import com.salsa.lottery.dto.request.user.UserCreateRequest;
import com.salsa.lottery.dto.request.user.UserSearchRequest;
import com.salsa.lottery.dto.response.ControllerResponse;
import com.salsa.lottery.dto.response.PageResponseWrapper;
import com.salsa.lottery.dto.response.user.UserResponse;
import com.salsa.lottery.entity.User;
import com.salsa.lottery.repository.UserRepository;
import com.salsa.lottery.service.UserService;
import com.salsa.lottery.utils.specification.UserSpecification;
import io.sentry.spring.tracing.SentrySpan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ControllerResponse<?> createNewUser(UserCreateRequest request) {
        String id = UUID.randomUUID().toString();
        userRepository.CreateNewUser(id, request.getName(), request.getEmail(), request.getAddress(), request.getPhoneNumber());

        User user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid Id"));

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .name(user.getUserName())
                .email(user.getUserEmail())
                .address(user.getUserAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();

        ControllerResponse<UserResponse> response = ControllerResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("User Created")
                .data(userResponse)
                .build();

        return response;
    }

    @Override
    public ControllerResponse<?> getAllUserWithPage(Pageable pageable, UserSearchRequest request) {
        Specification<User> specification = UserSpecification.getSpecification(request);
        Page<User> page = userRepository.findAll(specification, pageable);
        List<UserResponse> userResponseList = new ArrayList<>();

        for (User user : page){
            UserResponse response = UserResponse.builder()
                    .id(user.getId())
                    .address(user.getUserAddress())
                    .email(user.getUserEmail())
                    .name(user.getUserName())
                    .phoneNumber(user.getPhoneNumber())
                    .build();
            userResponseList.add(response);
        }

        PageResponseWrapper pageResponseWrapper = PageResponseWrapper.builder()
                .data(userResponseList)
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .size(page.getSize())
                .build();

        ControllerResponse<?> response = ControllerResponse.<PageResponseWrapper>builder()
                .message("Users List")
                .data(pageResponseWrapper)
                .build();

        return response;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    @SentrySpan
    public User getUserById(String id) {
        if (userRepository.findById(id).isPresent()){
            return userRepository.findById(id).get();
        }else throw new RuntimeException("DATA NOT FOUND");
    }
}
