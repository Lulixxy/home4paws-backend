package org.itsci.home4paws.controllers;

import org.itsci.home4paws.DTO.PostAnimalRequest;
import org.itsci.home4paws.DTO.PostAnimalResponse;
import org.itsci.home4paws.model.PostAnimal;
import org.itsci.home4paws.services.PostAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostAnimalController {

    @Autowired
    private PostAnimalService ps;

    //เพิ่มโพสต์
    @PostMapping("/add")
    public ResponseEntity<?> doAddPost(@RequestBody PostAnimalRequest post) {
        try {
            String result = ps.doAddPost(post.toEntity(), post.getUsername());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    //ดึงโพสต์ทั้งหมดของ user
    @GetMapping("/list/{username}")
    public ResponseEntity<List<PostAnimal>> getListPostByUser(@PathVariable String username) {
        List<PostAnimal> postList = ps.getListPostByUser(username);
        return ResponseEntity.ok(postList);
    }

    //ดึงโพสต์ตาม ID
    @GetMapping("/{animalId}")
    public ResponseEntity<PostAnimal> getPostById(@PathVariable String animalId) {
        PostAnimal post = ps.getPostById(animalId);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //แก้ไขโพสต์ตาม ID
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> doEditPost(@PathVariable("id") String animalId,
                                             @RequestBody PostAnimalRequest updatedRequest) {
        // รับค่าผลลัพธ์จาก Service
        String result = ps.editPost(animalId, updatedRequest);

        // เช็คค่าที่ส่งกลับมาเพื่อตอบกลับให้ถูกต้อง
        if ("update_success".equals(result)) {
            // กรณีแก้ไขสำเร็จ และไม่มีคำต้องห้าม
            return ResponseEntity.ok(result);
        } else if ("pending_review".equals(result)) {
            // กรณีแก้ไขสำเร็จ แต่เจอคำต้องห้าม (สถานะเปลี่ยนเป็นรอตรวจสอบ)
            return ResponseEntity.ok(result);
        } else {
            // กรณีหาโพสต์ไม่เจอ หรือ result เป็นอย่างอื่น
            return ResponseEntity.notFound().build();
        }
    }

    //ลบโพสต์ตาม ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> doDeletePostById(@PathVariable("id") String animalId) {
        try {
            ps.deletePostById(animalId);
            return ResponseEntity.ok("Post deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    //แสดงโพสต์ทั้งหมดที่สัตว์สถานะว่าง
    @GetMapping("/allAvailable")
    public ResponseEntity<List<PostAnimalResponse>> getListPost() {
        List<PostAnimalResponse> postList = ps.getListPost();
        return ResponseEntity.ok(postList);
    }

    //ค้นหาโพสต์ทั้งหมดที่สัตว์สถานะว่างตาม keyword
    @GetMapping("/search")
    public ResponseEntity<List<PostAnimalResponse>> doSearchPost(@RequestParam("q") String keyword) {
        List<PostAnimalResponse> results = ps.searchPosts(keyword);
        return ResponseEntity.ok(results);
    }

}
