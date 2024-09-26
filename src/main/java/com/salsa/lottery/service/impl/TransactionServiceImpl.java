package com.salsa.lottery.service.impl;

import com.salsa.lottery.dto.request.transaction.TransactionRequest;
import com.salsa.lottery.dto.request.transaction.TransactionSearchRequest;
import com.salsa.lottery.dto.response.ControllerResponse;
import com.salsa.lottery.dto.response.PageResponseWrapper;
import com.salsa.lottery.dto.response.lottery.LotteryResponse;
import com.salsa.lottery.dto.response.transaction.TransactionResponse;
import com.salsa.lottery.dto.response.transaction.WinnerResponse;
import com.salsa.lottery.dto.response.user.UserResponse;
import com.salsa.lottery.entity.Lottery;
import com.salsa.lottery.entity.Transaction;
import com.salsa.lottery.entity.User;
import com.salsa.lottery.repository.TransactionRepository;
import com.salsa.lottery.service.LotteryService;
import com.salsa.lottery.service.TransactionService;
import com.salsa.lottery.service.UserService;
import com.salsa.lottery.utils.specification.TransactionSpecification;
import com.salsa.lottery.utils.specification.UserSpecification;
import jdk.jfr.consumer.RecordedThread;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final LotteryService lotteryService;

    @Override
    public ControllerResponse<?> createNewTransaction(TransactionRequest request) {
        List<User> users = userService.getAllUser();
        Lottery lottery = lotteryService.getLotteryById(request.getLotteryId());

        Random random = new Random();
        int randomIndex = random.nextInt(users.size());
        User user = users.get(randomIndex);

        Transaction transaction = Transaction.builder()
                .lottery(lottery)
                .user(user)
                .winner(user.getUserName())
                .build();
        transactionRepository.save(transaction);

        WinnerResponse winnerResponse = WinnerResponse.builder()
                .transactionId(transaction.getId())
                .lotteryId(lottery.getId())
                .lotteryName(lottery.getLotteryName())
                .winner(transaction.getUser().getUserName())
                .build();

        ControllerResponse<WinnerResponse> response = ControllerResponse.<WinnerResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("Congratulation For The Winner !!! ")
                .data(winnerResponse)
                .build();

        return response;
    }

    @Override
    public ControllerResponse<?> getAllTransactionWithPage(Pageable pageable, TransactionSearchRequest request) {
        Specification<Transaction> specification = TransactionSpecification.getSpecification(request);
        Page<Transaction> page = transactionRepository.findAll(specification, pageable);
        List<TransactionResponse> transactionResponseList = new ArrayList<>();

        for (Transaction transaction : page){
            TransactionResponse response = TransactionResponse.builder()
                    .id(transaction.getId())
                    .user(transaction.getUser().getId()) //must be list (RAND)
                    .lottery(transaction.getLottery().getId()) //must be list
                    .build();
            transactionResponseList.add(response);
        }

        PageResponseWrapper pageResponseWrapper = PageResponseWrapper.builder()
                .data(transactionResponseList)
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .size(page.getSize())
                .build();

        ControllerResponse<?> response = ControllerResponse.<PageResponseWrapper>builder()
                .message("Transaction List")
                .data(pageResponseWrapper)
                .build();

        return response;
    }

    @Override
    public ControllerResponse<?> winnerUser(TransactionRequest request) {
//        create winner user from userList,
        return null;
    }
}
