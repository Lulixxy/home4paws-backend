package org.itsci.home4paws.repository;

import org.itsci.home4paws.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,String> {
    Member findByUsername(String username);
    // นับจำนวนสมาชิกที่โดนแบน (banStatus = true)
    long countByBanStatus(Boolean banStatus);
}
