package ru.lastuhina.terminal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lastuhina.terminal.service.impl.TerminalServiceImpl;

import java.math.BigDecimal;

@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
public class TerminalController {

    private final TerminalServiceImpl terminalServiceImplService;

    @GetMapping("/checkAccount")
    public ResponseEntity<BigDecimal> checkAccount() {

        return ResponseEntity.ok(terminalServiceImplService.getBalance());
    }

    @PostMapping("/withdrawMoney/{amount}")
    public ResponseEntity<BigDecimal> withdrawMoney(@PathVariable BigDecimal amount) {
        terminalServiceImplService.withdrawMoney(amount);

        return ResponseEntity.ok(terminalServiceImplService.withdrawMoney(amount));
    }

    @PostMapping("/depositMoney/{amount}")
    public ResponseEntity<String> depositMoney(@PathVariable BigDecimal amount) {
        terminalServiceImplService.depositMoney(amount);

        return ResponseEntity.ok("Успешная паплвава");
    }

}
