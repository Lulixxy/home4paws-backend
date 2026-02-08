package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.LoginRequest;
import org.itsci.home4paws.DTO.MemberRegRequest;
import org.itsci.home4paws.DTO.ShelterRegRequest;
import org.itsci.home4paws.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class MemberController {

    @Autowired
    private MemberService ms;

    //เข้าสู่ระบบ
    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest loginRequest) {
        try {
            String user = ms.doLogin(loginRequest.getUsername(), loginRequest.getPassword());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //ลงทะเบียนสมาชิก
    @PostMapping("/register/member")
    public ResponseEntity<String> register(@RequestBody MemberRegRequest request) {
        try {
            String result = ms.registerMember(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("register_failed");
        }
    }

    //ลงทะเบียนศูยน์พักพิง
    @PostMapping("/register/shelter")
    public ResponseEntity<String> register(@RequestBody ShelterRegRequest request) {
        try {
            String result = ms.registerShelter(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("register_failed");
        }
    }

}
