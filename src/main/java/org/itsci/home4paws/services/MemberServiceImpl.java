package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.MemberRegRequest;
import org.itsci.home4paws.DTO.ShelterRegRequest;
import org.itsci.home4paws.model.Member;
import org.itsci.home4paws.model.Shelter;
import org.itsci.home4paws.model.User;
import org.itsci.home4paws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    UserRepository mr;

    //เข้าสู่ระบบ
    @Override
    public String doLogin(String username, String password) {
        String result = "";
        User user = mr.findByUsername(username);

        if (user == null) {
            result = "nodata"; // ไม่มี username นี้
        } else if (!user.getPassword().equals(hashToMD5(password))) {
            result = "false"; // รหัสผ่านผิด
        } else if (Boolean.TRUE.equals(user.getBanStatus())) {
            result = "noactive"; // ถูกแบน
        } else {
            result = "true"; // login ผ่าน
        }

        System.out.println(result);
        return result;
    }

    //ลงทะเบียนสมาชิก
    @Override
    public String registerMember(MemberRegRequest req) {
        if (mr.findByUsername(req.getUsername()) != null) {
            return "duplicate_username";
        }

        if (mr.findByUsername(req.getEmail()) != null) {
            return "duplicate_email";
        }

        if (mr.findByUsername(req.getPhoneNumber()) != null) {
            return "duplicate_tel";
        }

        Member m = new Member();

        String latestId = mr.findMaxUserId();
        String newId;
        if (latestId == null) {
            newId = "U00001"; // เริ่มต้นถ้ายังไม่มีข้อมูล
        } else {
            // ดึงเลขจาก U00001 → 00001 แล้วเพิ่ม +1
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("U%05d", num);
        }
        m.setUserId(newId);
        m.setUsername(req.getUsername());
        m.setPassword(hashToMD5(req.getPassword()));
        m.setEmail(req.getEmail());
        m.setPhoneNumber(req.getPhoneNumber());
        m.setAddress(req.getAddress());
        m.setProfilePicture(req.getProfilePicture());

        m.setMemberType("member");
        m.setBanStatus(false);

        // set member-specific fields
        m.setFirstName(req.getFirstName());
        m.setLastName(req.getLastName());
        m.setGender(req.getGender());
        m.setAge(req.getAge());
        m.setDateOfBirth(req.getDateOfBirth());
        m.setProvince(req.getProvince());
        m.setDistrict(req.getDistrict());
        m.setSubDistrict(req.getSubDistrict());
        m.setPostalCode(req.getPostalCode());
        m.setAddressType(req.getAddressType());
        m.setIncome(req.getIncome());

        mr.save(m);
        return "register_success";
    }

    //ลงทะเบียนศูยน์พักพิง
    @Override
    public String registerShelter(ShelterRegRequest req) {
        if (mr.findByUsername(req.getUsername()) != null) {
            return "duplicate_username";
        }

        if (mr.findByUsername(req.getEmail()) != null) {
            return "duplicate_email";
        }

        if (mr.findByUsername(req.getPhoneNumber()) != null) {
            return "duplicate_tel";
        }

        Shelter s = new Shelter();

        String latestId = mr.findMaxUserId();
        String newId;
        if (latestId == null) {
            newId = "U00001"; // เริ่มต้นถ้ายังไม่มีข้อมูล
        } else {
            // ดึงเลขจาก U00001 → 00001 แล้วเพิ่ม +1
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("U%05d", num);
        }
        s.setUserId(newId);
        s.setUsername(req.getUsername());
        s.setPassword(hashToMD5(req.getPassword()));
        s.setEmail(req.getEmail());
        s.setPhoneNumber(req.getPhoneNumber());
        s.setAddress(req.getAddress());
        s.setProfilePicture(req.getProfilePicture());

        s.setMemberType("shelter");
        s.setBanStatus(false);

        // set shelter-specific fields
        s.setShelterName(req.getShelterName());
        s.setRegistrationNumber(req.getRegistrationNumber());
        mr.save(s);
        return "register_success";
    }

    //เอาไว้เข้ารหัส
    public static String hashToMD5(String input) {
        try {
            // Get an instance of the MD5 message digest algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the input string to bytes
            byte[] inputBytes = input.getBytes();

            // Update the message digest with the input bytes
            md.update(inputBytes);

            // Calculate the hash value
            byte[] hashBytes = md.digest();

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
