package org.itsci.home4paws.controllers;

import jakarta.servlet.http.HttpSession;
import org.itsci.home4paws.model.Admin;
import org.itsci.home4paws.model.Member;
import org.itsci.home4paws.model.PostAnimal;
import org.itsci.home4paws.model.Review;
import org.itsci.home4paws.repository.MemberRepository;
import org.itsci.home4paws.repository.PostAnimalRepository;
import org.itsci.home4paws.repository.ReviewRepository;
import org.itsci.home4paws.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostAnimalRepository postRepo;
    @Autowired private ReviewRepository reviewRepo;
    @Autowired private MemberRepository memberRepo;

    // หน้า Login
    @GetMapping("/login")
    public String showLoginPage() {
        return "admin-login"; // ชื่อไฟล์ html ที่จะสร้าง
    }

    // รับค่า Login
    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               Model model,
                               HttpSession session) {
        Admin admin = adminService.doAdminlogin(username, password);
        if (admin != null) {
            session.setAttribute("admin", admin); // ฝัง session ว่าล็อกอินแล้ว
            return "redirect:/admin/dashboard"; // ไปหน้าหลัก
        } else {
            model.addAttribute("error", "Username หรือ Password ไม่ถูกต้อง");
            return "admin-login"; // กลับมาหน้าเดิม
        }
    }

    // หน้า Dashboard (แสดงสถิติ)
    @GetMapping("/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        // --- ดึงตัวเลขสถิติ ---
        long totalMembers = memberRepo.count();             // สมาชิกทั้งหมด
        long bannedMembers = memberRepo.countByBanStatus(true); // สมาชิกที่โดนแบน
        long totalPosts = postRepo.count();                 // โพสต์ทั้งหมด
        long availablePosts = postRepo.countByAnimalStatus("Available"); // โพสต์ที่ยังว่าง

        // ส่งค่าไปหน้าเว็บ
        model.addAttribute("totalMembers", totalMembers);
        model.addAttribute("bannedMembers", bannedMembers);
        model.addAttribute("totalPosts", totalPosts);
        model.addAttribute("availablePosts", availablePosts);

        return "admin-dashboard"; // ไปหน้า Dashboard ใหม่
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    // หน้าจัดการโพสต์
    @GetMapping("/posts")
    public String showManagePosts(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        // ✅ แก้ตรงนี้: ดึงเฉพาะโพสต์ที่ไม่เหมาะสม (appropriate = false)
        List<PostAnimal> inappropriatePosts = postRepo.findByAppropriateFalse();

        System.out.println("-------------------------------------------------");
        System.out.println("⚠️ Admin Review: เจอโพสต์ไม่เหมาะสม " + inappropriatePosts.size() + " รายการ");
        System.out.println("-------------------------------------------------");

        model.addAttribute("animals", inappropriatePosts);

        return "admin-posts";
    }

    // ฟังก์ชันลบโพสต์ (Remove Post)
    @GetMapping("/delete-post/{id}")
    public String deletePost(@PathVariable("id") String id, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        // ลบข้อมูลจริง
        postRepo.deleteById(id);

        return "redirect:/admin/dashboard";
    }

    // หน้าแสดงรีวิวทั้งหมด (List Reviews) พร้อมตัวกรอง
    @GetMapping("/reviews")
    public String showReviews(@RequestParam(name = "filter", required = false) String filter, // รับค่า filter
                              Model model,
                              HttpSession session) {

        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        List<Review> reviews;

        // เช็คว่าถ้า User กดกรอง "bad" ให้ดึงเฉพาะคะแนน < 3.0
        if ("bad".equals(filter)) {
            reviews = reviewRepo.findByRatingLessThan(3.0);
            model.addAttribute("filterMode", "bad"); // ส่งค่าไปบอกหน้าเว็บว่ากำลังกรองอยู่
        } else {
            // ถ้าไม่กรอง ก็ดึงทั้งหมด
            reviews = reviewRepo.findAll();
            model.addAttribute("filterMode", "all");
        }

        model.addAttribute("reviews", reviews);
        return "admin-reviews";
    }

    // ฟังก์ชันแบนผู้ใช้ (Ban Adopter)
    @GetMapping("/ban-user/{username}")
    public String banUser(@PathVariable("username") String username, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        Member member = memberRepo.findByUsername(username);
        if (member != null) {
            member.setBanStatus(true);
            memberRepo.save(member);
        }

        return "redirect:/admin/reviews";
    }

    // ฟังก์ชันปลดแบน (เผื่อกดผิด)
    @GetMapping("/unban-user/{username}")
    public String unbanUser(@PathVariable("username") String username, HttpSession session) {
        if (session.getAttribute("admin") == null) return "redirect:/admin/login";

        Member member = memberRepo.findByUsername(username);
        if (member != null) {
            member.setBanStatus(false);
            memberRepo.save(member);
        }

        return "redirect:/admin/reviews";
    }
}