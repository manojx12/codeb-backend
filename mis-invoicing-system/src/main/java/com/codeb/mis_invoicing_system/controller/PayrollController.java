package com.codeb.mis_invoicing_system.controller;

import com.codeb.mis_invoicing_system.entity.Payroll;
import com.codeb.mis_invoicing_system.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {

    @Autowired
    private PayrollRepository payrollRepository;

    @PostMapping
    public Payroll createPayroll(@RequestBody Payroll payroll) {
        Double netPay = payroll.getBasicSalary() - payroll.getDeductions();
        payroll.setNetPay(netPay);
        return payrollRepository.save(payroll);
    }

    @PutMapping("/{id}")
    public Payroll updatePayroll(@PathVariable Long id, @RequestBody Payroll updatedPayroll) {
        Double netPay = updatedPayroll.getBasicSalary() - updatedPayroll.getDeductions();
        updatedPayroll.setNetPay(netPay);
        updatedPayroll.setId(id);
        return payrollRepository.save(updatedPayroll);
    }

    @GetMapping
    public List<Payroll> getAllPayroll() {
        return payrollRepository.findAll();
    }

    @GetMapping("/{id}")
    public Payroll getPayrollById(@PathVariable Long id) {
        return payrollRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String deletePayroll(@PathVariable Long id) {
        payrollRepository.deleteById(id);
        return "Payroll deleted with id: " + id;
    }
}