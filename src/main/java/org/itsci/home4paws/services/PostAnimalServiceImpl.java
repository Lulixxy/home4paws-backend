package org.itsci.home4paws.services;

import org.itsci.home4paws.DTO.PostAnimalRequest;
import org.itsci.home4paws.DTO.PostAnimalResponse;
import org.itsci.home4paws.model.PostAnimal;
import org.itsci.home4paws.model.User;
import org.itsci.home4paws.repository.UserRepository;
import org.itsci.home4paws.repository.PostAnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostAnimalServiceImpl implements PostAnimalService{

    @Autowired
    PostAnimalRepository pr;
    @Autowired
    UserRepository mr;

    // Method เช็คคำเผื่อแอบขายสัตว์/ติดต่อแอปนอก
    private boolean containsBannedWords(String text) {
        if (text == null || text.isEmpty()) return false;
        String[] bannedWords = {
                "ขาย", "ราคา", "บาท", "baht", "sale", "sell",
                "line", "id", "ไลน์", "facebook", "fb", "เฟส",
                "inbox", "ib", "08", "09", "06", "สินสอด"
        };
        String lowerText = text.toLowerCase();
        for (String word : bannedWords) {
            if (lowerText.contains(word)) {
                return true;
            }
        }
        return false;
    }

    //เพิ่มโพสต์
    @Override
    public String doAddPost(PostAnimal p, String username) {
        User user = mr.findByUsername(username);
        String latestId = pr.findMaxAnimalId();
        String newId;
        if (latestId == null) {
            newId = "A00001"; // เริ่มต้นถ้ายังไม่มีข้อมูล
        } else {
            // ดึงเลขจาก A00001 → 00001 แล้วเพิ่ม +1
            int num = Integer.parseInt(latestId.substring(1));
            num++;
            newId = String.format("A%05d", num);
        }
        p.setAnimalId(newId);
        p.setAnimalStatus("Available");
        p.setMember(user);

        // เช็คคำต้องห้าม
        boolean foundBannedWords = containsBannedWords(p.getAnimalName()) ||
                containsBannedWords(p.getPersonality()) ||
                containsBannedWords(p.getLocation());

        // ถ้าหน้าบ้านส่ง appropriate=false มา หรือ หลังบ้านตรวจเจอคำต้องห้ามเอง
        if (foundBannedWords || (p.getAppropriate() != null && !p.getAppropriate())) {
            p.setAppropriate(false); // ตั้งเป็น "ไม่เหมาะสม/รอตรวจสอบ"
            pr.save(p);
            return "pending_review"; // ส่งค่านี้กลับไปบอกหน้าบ้าน
        } else {
            p.setAppropriate(true); // ปกติ
            pr.save(p);
            return "add_success"; // ส่งค่านี้ถ้าผ่าน
        }
    }

    //ดึงโพสต์ทั้งหมดของ user
    @Override
    public List<PostAnimal> getListPostByUser(String username) {
        return pr.findByMemberUsername(username);
    }

    //ดึงโพสต์ตาม ID
    @Override
    public PostAnimal getPostById(String animalId) {
        return pr.findById(animalId).orElse(null);
    }

    //แก้ไขโพสต์ตาม ID
    @Override
    public String editPost(String animalId, PostAnimalRequest updatedRequest) {
        Optional<PostAnimal> optionalPost = pr.findById(animalId);

        if (optionalPost.isPresent()) {
            PostAnimal editP = optionalPost.get();

            // อัปเดตข้อมูลตาม Request
            editP.setAnimalImage(updatedRequest.getAnimalImage());
            editP.setAnimalName(updatedRequest.getAnimalName());
            editP.setGender(updatedRequest.getGender());
            editP.setBreed(updatedRequest.getBreed());
            editP.setAge(updatedRequest.getAge());
            editP.setPersonality(updatedRequest.getPersonality());
            editP.setLocation(updatedRequest.getLocation());
            editP.setAnimalType(updatedRequest.getAnimalType());

            // ตรวจสอบคำต้องห้ามใหม่อีกครั้ง (Re-validation)
            // เช็คจากข้อมูลใหม่ที่เพิ่ง set เข้าไป
            boolean foundBannedWords = containsBannedWords(editP.getAnimalName()) ||
                    containsBannedWords(editP.getPersonality()) ||
                    containsBannedWords(editP.getLocation());

            // อัปเดตสถานะความเหมาะสม (Appropriate)
            if (foundBannedWords) {
                editP.setAppropriate(false); // เจอคำต้องห้าม -> ปรับเป็นไม่เหมาะสม/รอตรวจสอบ
                pr.save(editP);
                return "pending_review"; // ส่ง status บอกหน้าบ้านว่าโพสต์นี้ต้องรอตรวจสอบนะ
            } else {
                editP.setAppropriate(true); // ไม่เจอ -> ปรับเป็นเหมาะสม (หรือปลดแบนถ้าแก้ไขแล้ว)
                pr.save(editP);
                return "update_success"; // แก้ไขและเผยแพร่สำเร็จ
            }

        } else {
            return "Post not found.";
        }
    }

    //ลบโพสต์ตาม ID
    @Override
    public void deletePostById(String animalId) {
        if (pr.existsById(animalId)) {
            pr.deleteById(animalId);
        } else {
            throw new RuntimeException("Post with ID " + animalId + " not found.");
        }
    }

    //แสดงโพสต์ทั้งหมดที่สัตว์สถานะว่าง
    @Override
    public List<PostAnimalResponse> getListPost() {
        List<PostAnimal> posts = pr.findAvailablePosts();
        return posts.stream()
                .map(PostAnimalResponse::new)
                .collect(Collectors.toList());
    }

    //ค้นหาโพสต์ทั้งหมดที่สัตว์สถานะว่างตาม keyword
    @Override
    public List<PostAnimalResponse> searchPosts(String keyword) {
        // ป้องกัน Error กรณีส่งค่าว่างมา
        if (keyword == null) {
            keyword = "";
        }
        // ตัดช่องว่างหน้า-หลังออกก่อน (เผื่อพิมพ์ "แมว ")
        keyword = keyword.trim();
        // แปลงคำค้นหาภาษาไทย -> อังกฤษ (เพื่อให้ตรงกับข้อมูลใน DB)
        if ("แมว".equals(keyword)) {
            keyword = "Cat"; // ใน DB เก็บ "Cat" หรือ "cat" เช็คตัวพิมพ์เล็กใหญ่ด้วยนะ
        } else if ("หมา".equals(keyword) || "สุนัข".equals(keyword)) {
            keyword = "Dog";
        }

        List<PostAnimal> posts = pr.searchPostsByKeyword(keyword);
        return posts.stream()
                .map(PostAnimalResponse::new)
                .collect(Collectors.toList());
    }

}
