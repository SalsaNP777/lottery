package com.salsa.lottery.controller;

import com.salsa.lottery.dto.request.transaction.TransactionRequest;
import com.salsa.lottery.dto.request.transaction.TransactionSearchRequest;
import com.salsa.lottery.dto.response.ControllerResponse;
import com.salsa.lottery.service.TransactionService;
import com.salsa.lottery.utils.constant.ApiUrlConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrlConstant.TRANSACTION)
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createNewTrasaction(@RequestBody TransactionRequest request){
        ControllerResponse<?> response = transactionService.createNewTransaction(request);

        ResponseEntity result = ResponseEntity.status(HttpStatus.CREATED)
                .body(response);

        return result;
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactionWithPage(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @ModelAttribute TransactionSearchRequest request
    ){
        Pageable pageable = PageRequest.of(page, size);
        ControllerResponse<?> response = transactionService.getAllTransactionWithPage(pageable,request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
