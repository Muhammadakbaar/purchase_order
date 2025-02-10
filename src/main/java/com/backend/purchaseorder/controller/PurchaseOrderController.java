package com.backend.purchaseorder.controller;

import com.backend.purchaseorder.dto.po.PurchaseOrderHeaderDTO;
import com.backend.purchaseorder.service.PurchaseOrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pos")
@Validated 
public class PurchaseOrderController {

    private final PurchaseOrderService poService;

    public PurchaseOrderController(PurchaseOrderService poService) {
        this.poService = poService;
    }

    @GetMapping
    public List<PurchaseOrderHeaderDTO> getAllPOs() {
        return poService.getAllPOs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderHeaderDTO> getPOById(@PathVariable Integer id) {
        Optional<PurchaseOrderHeaderDTO> po = poService.getPOById(id);
        return po.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PurchaseOrderHeaderDTO> createPO(@Valid @RequestBody PurchaseOrderHeaderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(poService.savePO(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderHeaderDTO> updatePO(@PathVariable Integer id, @Valid @RequestBody PurchaseOrderHeaderDTO poHDTO) {
        return poService.updatePO(id, poHDTO)
                .map(po -> ResponseEntity.<PurchaseOrderHeaderDTO>ok(po))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePO(@PathVariable Integer id) {
        return poService.deletePO(id) ? ResponseEntity.ok("Deleted successfully")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("PO not found");
    }
}


