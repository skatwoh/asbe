package org.example.asbe.repository;

import org.example.asbe.model.entity.Userinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<Userinfo, Integer> {
    Optional<Userinfo> findByEmail(String email); // Use 'email' if that is the correct field for login

    Optional<Userinfo> findByUsername(String username); // Use 'username' if that is the correct field for login
}

