package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.WellbeingRequest;
import org.itsci.home4paws.model.Wellbeing;
import org.itsci.home4paws.services.WellbeingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/well-being")
public class WellbeingController {

    @Autowired
    private WellbeingService ws;

    //สร้างการอัพเดทชีวิตสัตว์
    @PostMapping("/add")
    public ResponseEntity<Wellbeing> doAddWellbeing(@RequestBody WellbeingRequest request) {
        Wellbeing wellbeing = ws.doAddWellbeing(request);
        return ResponseEntity.ok(wellbeing);
    }

    //ดูข้อมูลการอัพเดทชีวิตสัตว์
    @GetMapping("/{wellbeingId}")
    public ResponseEntity<Wellbeing> getWellbeingById(@PathVariable String wellbeingId) {
        Wellbeing wellbeing = ws.getWellbeingById(wellbeingId);
        return ResponseEntity.ok(wellbeing);
    }

    // ดูอัพเดทชีวิตสัตว์ทั้งหมดของการรับเลี้ยงนี้
    @GetMapping("/list")
    public ResponseEntity<List<Wellbeing>> getTimelineByAdoptionId(@RequestParam String adoptionId) {
        List<Wellbeing> timeline = ws.getWellbeingByAdoptionId(adoptionId);
        return ResponseEntity.ok(timeline);

    }
}
