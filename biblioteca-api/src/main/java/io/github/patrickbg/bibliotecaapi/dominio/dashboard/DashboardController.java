package io.github.patrickbg.bibliotecaapi.dominio.dashboard;

import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.Dashboard;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Year;

@RestController
@RequestMapping("dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Dashboard> obterDashboard(
            @RequestParam(required = false)
            @PastOrPresent(message = "O ano não pode ser no futuro")
            Year ano) {
        Dashboard dashboard = dashboardService.obterDashboardPorAno(ano);
        return ResponseEntity.ok(dashboard);
    }
}
