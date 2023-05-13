package ru.lastuhina.terminal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lastuhina.terminal.service.impl.TerminalService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/terminal")
@RequiredArgsConstructor
@Slf4j
public class TerminalController {

    private final TerminalService terminalServiceService;

    @GetMapping("/checkAccount")
    public ResponseEntity<BigDecimal> checkAccount() {
        return ResponseEntity.ok(terminalServiceService.getBalance());
    }

    @PostMapping("/withdraw/{amount}")
    public ResponseEntity<String> withdrawMoney(@PathVariable BigDecimal amount) {
        terminalServiceService.withdrawMoney(amount);

        return ResponseEntity.ok("Деньги успешно сняты");
    }

    @PostMapping("/depositMoney/{amount}")
    public ResponseEntity<String> depositMoney(@PathVariable BigDecimal amount) {
        terminalServiceService.depositMoney(amount);

        return ResponseEntity.ok("Деньги успешно внесены");
    }

}
